package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.dto.RoomDto;
import com.sgu.schedulerApp.entity.Room;
import com.sgu.schedulerApp.exception.CustomErrorException;
import com.sgu.schedulerApp.repository.DepartmentRepository;
import com.sgu.schedulerApp.repository.RoomRepository;
import com.sgu.schedulerApp.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<RoomDto> findAll() {
        List<Room> rooms = roomRepository.findByStatusTrue();
        return rooms.stream().map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto findById(int id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            return modelMapper.map(room.get(), RoomDto.class);
        } else
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng học với id:"+id);
    }

    @Override
    public List<RoomDto> findByDepartmentCode(String keyword, String departmentCode) {
        keyword = keyword.isBlank() ? null:keyword;
        List<Room> rooms = roomRepository.findByKeywordAndDepartmentCode(keyword, departmentCode);
        return rooms.stream().map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomDto> findRoomNotOccupied(LocalDate date, LocalTime startTime, LocalTime endTime, String departmentCode, int eventId) {
        departmentCode = departmentCode.isBlank()? null:departmentCode;
        List<Room> rooms = roomRepository.findRoomNotOccupied(date, startTime, endTime, departmentCode, eventId);
        return rooms.stream().map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoomDto saveRoom(RoomDto roomDto) {
        Optional<Room> optionalRoom = roomRepository.findById(roomDto.getId());
        Room room;
        if (optionalRoom.isPresent()) {
            room = optionalRoom.get();
            if (checkRoomHasChange(room, roomDto)) {
                if (checkValidSaveRoom(roomDto))
                    throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE, "Mã phòng đã tồn tại trong hệ thống.");
            }
        } else {
            room = new Room();
            if (checkValidSaveRoom(roomDto))
                throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE, "Mã phòng đã tồn tại trong hệ thống.");
            room.setStatus(true);
        }
        room.setCode(roomDto.getCode().toUpperCase());
        room.setDepartment(departmentRepository.findByCodeAndStatusTrue(roomDto.getDepartmentCode()));
        Room savedRoom = roomRepository.save(room);
        return modelMapper.map(savedRoom, RoomDto.class);
    }

    private boolean checkValidSaveRoom(RoomDto roomDto) {
        return roomRepository.existsByCodeAndDepartment_CodeAndStatusTrue(
                roomDto.getCode(), roomDto.getDepartmentCode());
    }

    private boolean checkRoomHasChange(Room oldRoom, RoomDto newRoom) {
        return !( oldRoom.getCode().equals(newRoom.getCode()) &&
                oldRoom.getDepartment().getCode().equals(newRoom.getDepartmentCode()) );
    }

    @Override
    @Transactional
    public void deleteRoom(int id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            Room deletedRoom = room.get();
            if (!checkValidDeleteRoom(deletedRoom.getId())) {
                deletedRoom.setStatus(false);
                roomRepository.save(deletedRoom);
            } else
                throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE,
                        "Không thể xóa phòng nảy do có ít nhất môt sự kiện đã được đăng ký tổ chức sắp tới");
        } else
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng với id:"+id);
    }

    private boolean checkValidDeleteRoom(int roomId) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        return roomRepository.existsByIdAndEvents_DateGreaterThanOrEvents_DateEqualsAndEvents_StartTimeGreaterThanEqual(
                roomId, currentDate, currentDate, currentTime);
    }
}
