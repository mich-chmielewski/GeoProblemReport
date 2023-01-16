// Scripts

window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

});

function getMainContent(page) {
    isLogged();
    if (page === null) page = 'dashboard';
    if (event) event.preventDefault();
    window.history.replaceState("","Loaded Page",page);
    var url = '/manager/' + page.toLowerCase();
    $.post(url,null,function(data, textStatus, jqXHR) {
        $('#replacedMain').html(data);
        delete table;
        if($('#simpleTable').length){
          table = $('#simpleTable').DataTable({
          language: { url: './js/datatable.pl.json' },
           responsive: true
          });
        }
        if($('#reportTable').length){
           table = runReportTable();
        }
        if($('#typeBarChart').length && $('#typeBarChart').length){
           runCharts();
        }

    }).fail(function(jqXHR){
       showAlert("Błąd", jqXHR.responseJSON.error, jqXHR.status);
    });
};

function getJsonFromForm(formId) {
  var params = {};
  $('#'+formId +' input, #' + formId + ' select, #'+ formId +' textarea, #' + formId +' checkbox').each(
      function(index){
          var input = $(this);
          if (input[0].type === "checkbox" && input[0].checked) {
            params[input.attr('name')] = true;
            return;
          }
          params[input.attr('name')] = input.val();
      }
  );
  return JSON.stringify(params);
}

function hideModal(){
  $('.modal-backdrop').remove();
  $('.modal').modal('hide');
}

function showAlert(msgTitle, msgBody, msgType) {
    $('#alertMsg').find('strong').html(msgTitle + " Status: " + msgType);
    $('#alertMsg').find('p').html(msgBody);
    $('#alertMsg').show();
}

/* User operations */

async function isLogged() {
    let url = '/authorize';
    let response = await fetch(url);
    if (response.status === 200) {
     let data = await response.text();
     if (data == 'false') {
        window.location.href = "/login?expired";
     }
    }
}

function userModal(){
  $('#userForm' ).trigger("reset");
  $('#operation').val('post');
  $('#userModal').modal('show');
};

function sendUserForm(){
  event.preventDefault();
  var formData = getJsonFromForm('userForm');
    $.ajax({
        type: $('#operation').val(),
        crossDomain: true,
        url: "/manager/api/user/",
        data: formData,
        contentType: "application/json"
    })
    .done(function(data, textStatus, jqXHR){
      hideModal();
      getMainContent('user');
    })
    .fail(function(jqXHR, error, message){
       hideModal();
       showAlert(error, jqXHR.responseJSON.message, jqXHR.status);
    });
   };


async function getUser(id) {
    event.preventDefault();
    let url = '/manager/api/user/' + id;
    let response = await fetch(url);
    if (response.status === 200) {
        let data = await response.json();
        $('#id').val(data['id']);
        $('#username').val(data['username']);
        $('#password').val(data['password']);
        $('#userRole').val(data['userRole']).change();
        $('#email').val(data['email']);
        $('#enabled').val(data['enabled'].toString()).change();
        $('#operation').val('put');
        $('#userModal').modal('show');
    }
};

 function deleteUser(id) {
    event.preventDefault();
    bootbox.confirm("Potwierdzam usunięcie?", function(result) {
     if(result){
            let url = '/manager/api/user/' + id;
            fetch(url,{
               method: 'DELETE',
               headers: {
                 'Content-Type': 'application/json'
               },
               body: null
           })
           .then((response) => {
                   if (!response.ok) {
                       const error = response.status;
                       return Promise.reject(error);
                   }
                    getMainContent('user');
           })
           .catch( error  => {
                    showAlert('Error', 'ResourceNotFound', error);
           });
      }
    });
};

/* reportType operations */

function reportTypeModal(){
  $('#reportTypeForm' ).trigger("reset");
  $('#operation').val('post');
  $('#reportTypeModal').modal('show');
};

function sendReportTypeForm(){
  event.preventDefault();
  var formData = getJsonFromForm('reportTypeForm');
    $.ajax({
        type: $('#operation').val(),
        crossDomain: true,
        url: "/manager/api/reporttype/",
        data: formData,
        contentType: "application/json"
    })
    .done(function(data, textStatus, jqXHR){
      hideModal();
      getMainContent('reportType');
    })
    .fail(function(jqXHR, error, message){
       hideModal();
       showAlert(error, jqXHR.responseJSON.message, jqXHR.status);
    });
   };

async function getReportType(id) {
    event.preventDefault();
    let url = '/manager/api/reporttype/' + id;
    let response = await fetch(url);
    if (response.status === 200) {
        let data = await response.json();
        $('#id').val(data['id']);
        $('#operation').val('put');
        $('#active').val(data['active'].toString()).change();
        $('#description').val(data['description']);
        $('#email').val(data['email']);
        $('#reportTypeModal').modal('show');
    }
};

function deleteReportType(id) {
    event.preventDefault();
    bootbox.confirm("Potwierdzam usunięcie?", function(result) {
     if(result){
        let url = '/manager/api/reporttype/' + id;
         fetch(url,{
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json'
            },
            body: null
            })
             .then((response) => {
               if (response.ok) {
                 return getMainContent('reportType');
               }
               return Promise.reject(response);
             })
             .catch((response) => {
                 response.json().then((json) => {
                     showAlert('Error', json.message, json.code);
                 })
             });
      }
    });
};

/* ReportStatus operations */

function reportStatusModal(){
  $('#reportStatusForm' ).trigger("reset");
  $('#operation').val('post');
  $('#reportStatusModal').modal('show');
};

function sendReportStatusForm(){
  event.preventDefault();
  var formData = getJsonFromForm('reportStatusForm');
    $.ajax({
        type: $('#operation').val(),
        crossDomain: true,
        url: "/manager/api/status/",
        data: formData,
        contentType: "application/json"
    })
    .done(function(data, textStatus, jqXHR){
      hideModal();
      getMainContent('status');
    })
    .fail(function(jqXHR, error, message){
       hideModal();
       showAlert(error, jqXHR.responseJSON.message, jqXHR.status);
    });
   };

async function getReportStatus(id) {
    event.preventDefault();
    let url = '/manager/api/status/' + id;
    let response = await fetch(url);
    if (response.status === 200) {
        let data = await response.json();
        $('#id').val(data['id']);
        $('#operation').val('put');
        $('#statusCode').val(data['statusCode']);
        $('#description').val(data['description']);
        $('#color').val(data['color']);
        $('#stage').val(data['stage'].toString()).change();
        $('#reportStatusModal').modal('show');
    }
};

function deleteReportStatus(id) {
    event.preventDefault();
    bootbox.confirm("Potwierdzam usunięcie?", function(result) {
     if(result){
        let url = '/manager/api/status/' + id;
        fetch(url,{
           method: 'DELETE',
           headers: {
             'Content-Type': 'application/json'
           },
           body: null
           })
            .then((response) => {
              if (response.ok) {
                return getMainContent('status');
              }
              return Promise.reject(response);
            })
            .catch((response) => {
                response.json().then((json) => {
                    showAlert('Error', json.message, json.code);
                })
            });
        }
    });
};

/* Reports */

function reload(){
  table.ajax.reload();
}
function format ( d ) {
    return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+
        '<tr>'+
            '<td>Lat: ' + d.lat +'</td>'+
             '<td>Lon: '+d.lon+'</td>'+
            '<td><button type="button" onclick="showMap(' + d.id+ ');" class="btn btn-primary" data-toggle="modal">Lokalizacja</button></td>'+
             '<td><button type="button" onclick="showComments(' + d.id+ ');" class="btn btn-primary" data-toggle="modal">Komentarze</button></td>'+
        '</tr>'+
    '</table>'
}
function runReportTable() {

    $('#reportTable').on('click', 'tbody td.dt-control', function () {
        var tr = $(this).closest('tr');
        var row = table.row( tr );

        if ( row.child.isShown() ) {
            row.child.hide();
        }
        else {
            row.child( format(row.data()) ).show();
        }
    } );

    $('#reportTable').on('requestChild.dt', function(e, row) {
        row.child(format(row.data())).show();
    });

   table = $('#reportTable').DataTable( {
   language: {
           url: './js/datatable.pl.json'
       },
        responsive: true,
        "processing": true,
        "serverSide": true,
        "order": [ 1, "desc" ],
        "ajax": {
            url: "/manager/api/report/datatable",
            method: "POST"
         },
        "columns": [
        {
            "className":      'dt-control',
            "orderable":      false,
            "data":           '',
            "defaultContent": ''
        },
        { "data": "id" },
        { "data": "reportStatus",
          "className":      'dt-column-nowrap',
           render: function(data,type,row) {
              return '<a href="#" onclick="getReport(' + row.id +')" class="btn btn-outline-primary btn-sm" role="button"><i class="fas fa-pencil-alt"></i></a> '
                      + data;
           }

        },
        { "data": "reportType" },
        { "data": "message" },
        { "data": "photoPath",
            render: function(data, type) {
                    if (data != null) {
                        return '<a class="btn btn-outline-primary btn-sm" role="button" target="_blank" href="/files/' + data + '"><i class="fas fa-image"></i></a>';
                      } else {
                        return '';
                      }
            }
         },
        { "data": "email" },
        {
            "orderable":      false,
            "data":           "id",
            "defaultContent": '',
             render: function(data, type) {
                   return '<a href="#" role="button" onclick="deleteReport(' + data +')" class="btn btn-outline-primary btn-sm" aria-disabled="true"><i class="fas fa-trash-alt"></i></a>';
             }
        }
    ]
    });

$('#statusCode').change(function () {
        var statusCode = $('option:selected',this).val();
        var reportId = $('#id').val();
        saveStatus(reportId,statusCode);
});

    getReportStatusList();
    return table;
}

  async function saveStatus(id,statusCode) {
    let url = '/manager/api/report/' + id + '/' + statusCode;
    let response = await fetch(url,{
       method: 'PUT'
    });
    reload();
    $('#reportModal').modal('hide');
  };

function showMap(id){
  var url = '/map/map.html?search=' + id;
  $('#mapIframe').attr('src',url);
  $('#mapModal').modal('show');
};

function showAddComment(id){
  hideModal();
  $('#commentForm :input[name="id"]').val('');
  $('#commentForm :input[name="reportId"]').val(id);
  $('#commentForm :input[name="operation"]').val('post');
  $('#commentForm :input[name="content"]').val('');
  $('#commentForm :input[name="sendByEmail"]').prop("checked", false);
  $('#commentModal').modal('show');
}

function showComments(id){
  hideModal();
  $('#commentsModal').modal('show');
  getReportComments(id);
  let el = document.querySelector('#addComment');
  el.dataset.reportId = id;
}

async function getReportComments(id) {
    //event.preventDefault();
    let url = '/manager/api/comment/report/' + id;
    let response = await fetch(url);
    if (response.status === 200) {
        let data = await response.json();
        let tableBody = document.getElementById("commentsTable").tBodies[0];
        tableBody.innerHTML='';
        for (let row in data){
        let emailInfo = data[row].sendByEmail ? "<i class=\"fa fa-envelope\" title=\"Wysłany komentarz do zgłaszającego\"></i> " : "";
        tableBody.innerHTML += "<tr><td>" + data[row].id + "</td><td>"+data[row].updated + "</td><td>"+ emailInfo + data[row].content + "</td>"+"<td><a href=\"#\" onclick=\"editComment(" + data[row].id +"," + id + ")\" class=\"btn btn-outline-primary btn-sm\" role=\"button\"><i class=\"fas fa-pencil-alt\"></i></a> <a href=\"#\" onclick=\"deleteComment(" + data[row].id +"," + id + ")\" class=\"btn btn-outline-primary btn-sm\" role=\"button\"><i class=\"fas fa-trash-alt\"></i></a></td>"+"</tr>";
        }
    }
};

function sendCommentForm(){
  event.preventDefault();
  var formData = getJsonFromForm('commentForm');
    $.ajax({
        type: JSON.parse(formData).operation,
        crossDomain: true,
        url: "/manager/api/comment",
        data: formData,
        contentType: "application/json"
    })
    .done(function(data, textStatus, jqXHR){
      hideModal();
      showComments(data.reportId);
    })
    .fail(function(jqXHR, error, message){
       hideModal();
       showAlert(error, jqXHR.responseJSON.message, jqXHR.status);
    });
};

function deleteComment(id, reportId) {
    event.preventDefault();
    bootbox.confirm("Potwierdzam usunięcie?", function(result) {
     if(result){
        let url = '/manager/api/comment/' + id;
        fetch(url,{
           method: 'DELETE',
           headers: {
             'Content-Type': 'application/json'
           },
           body: null
           })
           .then((response) => {
                   if (!response.ok) {
                       const error = response.status;
                       return Promise.reject(error);
                   }
                    getReportComments(reportId);
           })
           .catch( error  => {
                    showAlert('Error', 'ResourceNotFound', error);
           });
      }
    });
};

    async function editComment(id) {
      event.preventDefault();
      let url = '/manager/api/comment/' + id;
      let response = await fetch(url);
      if (response.status === 200) {
          hideModal();
          let data = await response.json();
          $('#commentForm :input[name="id"]').val(data['id']);
          $('#commentForm :input[name="reportId"]').val(data['reportId']);
          $('#commentForm :input[name="operation"]').val('put');
          $('#commentForm :input[name="content"]').val(data['content']);
          $('#commentModal').modal('show');
      }
    };

  async function getReport(id) {
    event.preventDefault();
    let url = '/manager/api/report/' + id;
    let response = await fetch(url);
    if (response.status === 200) {
        let data = await response.json();
        $('#id').val(data['id']);
        $('#operation').val('put');
        $('#statusCode option[value='+ data['reportStatusDTO'].statusCode +']').attr('selected','selected');
        $('#reportModal').modal('show');
    }
  };

function deleteReport(id) {
    event.preventDefault();
    bootbox.confirm("Potwierdzam usunięcie?", function(result) {
     if(result){
        let url = '/manager/api/report/' + id;
        fetch(url,{
           method: 'DELETE',
           headers: {
             'Content-Type': 'application/json'
           },
           body: null
           })
           .then((response) => {
                   if (!response.ok) {
                       const error = response.status;
                       return Promise.reject(error);
                   }
                    reload();
           })
           .catch( error  => {
                    showAlert('Error', 'ResourceNotFound', error);
           });
      }
    });
};

async function getReportStatusList() {
let url = '/manager/api/status/list';
let response = await fetch(url);
if (response.status === 200) {
  let data = await response.json();
  for (i in data) {
    $('#statusCode').append('<option value="' +data[i].statusCode+ '">' +data[i].description+ '</option>');
  }
}
};

Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';

// Type Chart
function runCharts(){
    var ctxType = document.getElementById("typeBarChart");
    var typeLineChart = new Chart(ctxType, {
      type: 'bar',
      data: {
        labels: [],
        datasets: [{
          label: "Ilość",
          backgroundColor: "rgba(2,117,216,1)",
          borderColor: "rgba(2,117,216,1)",
          data: [],
        }],
      },
      options: {
        scales: {
          xAxes: [{
            time: {
              unit: 'typ'
            },
            gridLines: {
              display: false
            },
            ticks: {
                 callback: function(label) {
                       if (/\s/.test(label)) {
                         return label.split(" ");
                       }else{
                         return label;
                       }
                   }
            }
          }],
          yAxes: [{
            ticks: {
              min: 0,
              maxTicksLimit: 5
            },
            gridLines: {
              display: true
            }
          }],
        },
        legend: {
          display: false
        }
      }
    });

    // Type Chart
    var ctxStatus = document.getElementById("statusBarChart");
    var statusLineChart = new Chart(ctxStatus, {
      type: 'bar',
      data: {
        labels: [],
        datasets: [{
          label: "Ilość",
          backgroundColor: "rgba(2,117,216,1)",
          borderColor: "rgba(2,117,216,1)",
          data: [],
        }],
      },
      options: {
        scales: {
          xAxes: [{
            time: {
              unit: 'status'
            },
            gridLines: {
              display: false
            },
            ticks: {
                 callback: function(label) {
                       if (/\s/.test(label)) {
                         return label.split(" ");
                       } else {
                         return label;
                       }
                   }
            }
          }],
          yAxes: [{
            ticks: {
              min: 0,
              maxTicksLimit: 5
            },
            gridLines: {
              display: true
            }
          }],
        },
        legend: {
          display: false
        }
      }
    });

    ajax_chart(typeLineChart, '/manager/api/report/typechartdata');
    ajax_chart(statusLineChart, '/manager/api/report/statuschartdata');

    async function ajax_chart(chart, url, data) {
            var data = data || {};
        let response = await fetch(url);
        if (response.status === 200) {
            let data = await response.json();
                const keys = [];
                const vals = [];
                for (var i=0; i<data.length; i++) {
                    keys[i] =  Object.keys(data[i])[0];
                    vals[i] =  Object.values(data[i])[0];
                }
                chart.data.labels = keys;
                chart.data.datasets[0].data = vals;
                chart.update();
        }
    }

}