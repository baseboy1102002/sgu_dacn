function isValidDate(d) {
    return d instanceof Date && !isNaN(d);
}

function getParentElement(element, parent) {
    while (element.parentElement) {
        if (element.parentElement.matches(parent)) {
            return element.parentElement;
        }
        element = element.parentElement;
    }
}

$(document).ready(function () {
    let faculty_data = $.map(faculties, function (obj) {
        obj.id = obj.code
        obj.text = obj.name
        return obj;
    });
    let class_data = $.map(classrooms, function (obj) {
        obj.id = obj.code
        obj.text = obj.name
        return obj;
    });
    let department_data = $.map(departments, function (obj) {
        obj.id = obj.code
        obj.text = obj.name
        return obj;
    });
    let room_data = $.map(rooms, function (obj) {
        obj.id = obj.code
        obj.text = obj.code
        return obj;
    });
    $("#faculty_select").select2({
        data: faculty_data
    })
    $("#class_select").select2({
        data: class_data
    })
    $("#department_select").select2({
        data: department_data
    })
    $("#room_select").select2({
        data: room_data
    })

    $('#department_select').on('select2:select', function (e) {
        let data = e.params.data
        let departmentCode = data.id
        if (departmentCode === "") {
            room_data = $.map(rooms, function (obj) {
                obj.id = obj.code
                obj.text = obj.code
                return obj;
            });
            room_data.unshift({id:"", text:"Tất cả"})
        }
        else {
            room_data = rooms.reduce( (acc, item) => {
                if (item.departmentCode === departmentCode) {
                    let obj = {id: item.code, text: item.code}
                    acc.push(obj)
                }
            return acc}, [{id:"", text:"Tất cả"}])
        }
        $("#room_select").empty()
        $("#room_select").select2({
            data: room_data
        })
    });

    $('#faculty_select').on('select2:select', function (e) {
        let data = e.params.data
        let facultyCode = data.id
        if (facultyCode === "") {
            class_data = $.map(classrooms, function (obj) {
                obj.id = obj.code
                obj.text = obj.name
                return obj;
            });
            class_data.unshift({id:"", text:"Tất cả"})
        } else {
            class_data = classrooms.reduce( (acc, item) => {
                if (item.facultyCode === facultyCode) {
                    let obj = {id: item.code, text: item.name}
                    acc.push(obj)
                }
            return acc}, [{id:"", text:"Tất cả"}])
        }
        $("#class_select").empty()
        $("#class_select").select2({
            data: class_data
        })
    })

    $("#keyword_input").val($("input[name='keyword']").val());
    $("#department_select").val($("input[name='filterDto.departmentCode']").val());
    $('#department_select').trigger('change')
    $("#room_select").val($("input[name='filterDto.roomCode']").val());
    $('#room_select').trigger('change')
    $("#faculty_select").val($("input[name='filterDto.facultyCode']").val());
    $('#faculty_select').trigger('change')
    $("#class_select").val($("input[name='filterDto.classCode']").val());
    $('#class_select').trigger('change')
    $("#date_start").val($("input[name='filterDto.startTime']").val());
    $("#date_end").val($("input[name='filterDto.endTime']").val());
    let check = $("input[name='filterDto.isOnlyAttainable']").val() === "true"
    $("#isOnlyAttainable_checkbox").prop('checked', check)

    window.pagObj = $('#pagination').twbsPagination({
        totalPages: (totalPage==0)?1:totalPage,
        visiblePages: 5,
        startPage: currentPage,
        onPageClick: function (event, page) {
            if (currentPage != page) {
                $('#page').val(page);
                $('#searchform').submit();
            }
        }
    });

    $('#button-search , #button-filter').click(function (e) {
        e.preventDefault();
        $("input[name='page']").val(1);
        $('#searchform').submit();
    })

    $(".attend_btn").each(function (index, element) {
        $(element).on('click', function(e) {
            let eventId = $(getParentElement(element, '.attend_form')).data('event-id')
            $('#attend_modal').data('event-id', eventId)
            $('#attend_modal').modal('show')
        })
    })

    $('#attend_confirm').click(function (e) {
        e.preventDefault();
        let eventId = $('#attend_modal').data('event-id')
        $(".attend_form[data-event-id='"+eventId+"']").submit()
    })

    $(".dismiss_btn").each(function (index, element) {
        $(element).on('click', function(e) {
            let eventId = $(getParentElement(element, '.dismiss_form')).data('event-id')
            $('#dismiss_modal').data('event-id', eventId)

            $('#dismiss_modal').modal('show')
        })
    })

    $('#dismiss_confirm').click(function (e) {
        e.preventDefault();
        let eventId = $('#dismiss_modal').data('event-id')
        $(".dismiss_form[data-event-id='"+eventId+"']").submit()
    })

    $(".delete_btn").each(function (index, element) {
        $(element).on('click', function(e) {
            let eventId = $(getParentElement(element, '.delete_form')).data('event-id')
            $('#delete_modal').data('event-id', eventId)
            $('#delete_modal').modal('show')
        })
    })

    $('#delete_confirm').click(function (e) {
        e.preventDefault();
        let eventId = $('#delete_modal').data('event-id')
        $(".delete_form[data-event-id='"+eventId+"']").submit()
    })

})

$("#searchform").submit(function (e) {
    let st = new Date($("#date_start").val())
    let et = new Date($("#date_end").val())
    startTime = isValidDate(st) ? $("#date_start").val():""
    endTime = isValidDate(et) ? $("#date_end").val():""
    if ( (!isValidDate(st) || !isValidDate(et)) || startTime>=endTime ) {
        if ( !(!isValidDate(st) && !isValidDate(et)) )
            return false;
    }
    $("input[name='keyword']").val($("#keyword_input").val());
    $("input[name='filterDto.departmentCode']").val($("#department_select").val());
    $("input[name='filterDto.roomCode']").val($("#room_select").val());
    $("input[name='filterDto.facultyCode']").val($("#faculty_select").val());
    $("input[name='filterDto.classCode']").val($("#class_select").val());
    $("input[name='filterDto.isOnlyAttainable']").val($("#isOnlyAttainable_checkbox").is(':checked'));
    $("input[name='filterDto.startTime']").val(startTime);
    $("input[name='filterDto.endTime']").val(endTime);
    return true
});