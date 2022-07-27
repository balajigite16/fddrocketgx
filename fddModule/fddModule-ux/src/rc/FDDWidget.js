require.config({
    paths: {
        "datatables.net": "/module/fddModule/rc/scripts/jquery.dataTables.min",
        "datatables.net-bs4": "/module/fddModule/rc/scripts/dataTables.bootstrap4.min",
        "datatables.net-buttons": "/module/fddModule/rc/scripts/dataTables.buttons.min",
    }
});

define(['baja!',
        'bajaux/mixin/subscriberMixIn',
        'bajaux/Widget',
        'jquery',
        'Promise',
        'bajaux/events',
        'underscore',
        'hbs!nmodule/fddModule/rc/template/FddTemplate',
        'nmodule/fddModule/rc/scripts/jquery-3.5.1',
        'datatables.net',
        'datatables.net-bs4',
        'datatables.net-buttons',
        'nmodule/fddModule/rc/scripts/buttons.bootstrap4.min',
        'nmodule/fddModule/rc/scripts/buttons.colVis.min',
        'nmodule/fddModule/rc/scripts/buttons.html5.min',
        'nmodule/fddModule/rc/scripts/jszip.min',
        'nmodule/fddModule/rc/scripts/pdfmake.min',
        'nmodule/fddModule/rc/scripts/vfs_fonts',
        'nmodule/fddModule/rc/scripts/bootstrap.bundle.min',
        'css!nmodule/fddModule/rc/css/FDDWidget',
        'css!nmodule/fddModule/rc/css/bootstrap',
        'css!nmodule/fddModule/rc/css/bootstrap.min',
        'css!nmodule/fddModule/rc/css/dataTables.bootstrap4.min',
        'css!nmodule/fddModule/rc/css/buttons.bootstrap4.min',
        ],function(
         baja,
         subscriberMixIn,
         Widget,
         $,
         Promise,
         events,
         _,
         template
        ){

        'use strict';
        var compSub = new baja.Subscriber(),
                      props,
                      field,
                      datasetValue,
                      arc,
                      gradient;
        var gap = 2;
        var ranDataset = function () {
          var ran = Math.random();

          return    [
            {index: 0, name: 'move', icon: "", percentage: ran * 60 + 30},
          ];

        };

        var ranDataset2 = function () {
          var ran = Math.random();

          return    [
            {index: 0, name: 'move', icon: "", percentage: ran * 60 + 30}
          ];

        };
        function gaugeProperties() {
            return [
               {name: 'tableBackground', value: '#000000', typeSpec: 'gx:Color'},
               {name: 'tableFont',  value: null, typeSpec: 'gx:Font'}
            ];
        }

        var FDDWidget = function(){
             var that = this,
                     options;

             Widget.apply(that, arguments);

             that.options = options || {};
             that.options.margin = that.options.margin || {
               top: 30,
               right: 60,
               bottom: 45,
               left: 60
             };

             that.$title = null;
             that.margin = that.options.margin;

           this.properties().addAll(gaugeProperties());
           subscriberMixIn(this);
        };

        FDDWidget.prototype = Object.create(Widget.prototype);
        FDDWidget.prototype.constructor = FDDWidget;

        FDDWidget.prototype.doInitialize = function(dom){
             var that = this,
                prop = that.properties(),
                tableBackground = prop.get("tableBackground").value,
                fontValue = prop.get("tableFont").value,
                fontWight = "",
                fontTextDecoration = "",
                fontFamilyValue = "",
                fontStyle = "",
                fontSize="";
                if(fontValue!=="null" && fontValue!==null){
                    var fontSplitArray=fontValue.split(" "),
                    fontObj = {},
                    fontFamily = "";
                    for(var i=0;i<fontSplitArray.length;i++){
                        if(fontSplitArray[i]==="bold"){
                            fontWight = fontSplitArray[i].toString();
                        }
                        else if(fontSplitArray[i]==="italic"){
                            fontStyle = fontSplitArray[i].toString();
                        }
                        else if(fontSplitArray[i]==="underline"){
                            fontTextDecoration = fontSplitArray[i].toString();
                        }
                        else if(fontSplitArray[i].match(/\d+/)!==null && fontSplitArray[i].indexOf("pt")>-1){
                            fontSize = parseInt(fontSplitArray[i].toString().match(/\d+/)[0]);
                        }
                        else{
                            if(fontFamily!==""){
                                fontFamily = fontFamily +" "+ fontSplitArray[i].toString();
                            }
                            else
                            {
                                fontFamily = fontSplitArray[i].toString();
                            }
                        }
                    }
                }
                var jsonObj = [];
                render(jsonObj, dom);
        };

       /*
       *  Main function that renders a Iframe Page Content
       */
       function render(jsonObj, dom) {

             dom.html(template({}));
             var table = $('#example').DataTable({
                lengthChange: false,
                paging: true,
                scrollCollapse: false,
                autoWidth: false,
                scrollX: true,
                data: jsonObj,
                buttons: [ 'csv', 'copy', 'colvis' ],
                columns: [
                        { title: "STATUS" },
                        { title: "ASSET" },
                        { title: "FAULT" },
                        { title: "ACTIVATION" },
                        { title: "DEACTIVATION" },
                        { title: "DURATION" },
                        { title: "COST ANNUALISED" },
                        { title: "ENERGY" },
                        { title: "CO2 EMISSION" }
                ],
                "createdRow": function(row, data, dataIndex) {

                    $(row)[0].children[0].style.padding = "0.8em";
                    $(row)[0].children[1].style.padding = "0.8em";
                    $(row)[0].children[2].style.padding = "0.8em";
                    $(row)[0].children[3].style.padding = "0.8em";
                    $(row)[0].children[4].style.padding = "0.8em";
                    $(row)[0].children[5].style.padding = "0.8em";
                    $(row)[0].children[6].style.padding = "0.8em";
                    $(row)[0].children[7].style.padding = "0.8em";
                    $(row)[0].children[8].style.padding = "0.8em";


                    var subText = objDescription[$(row)[0].children[2].innerHTML.toUpperCase()]
                    if (typeof subText === "undefined") {
                        subText = "NA"
                    }

                    $(row)[0].children[2].innerHTML = $(row)[0].children[2].innerHTML + "<span class='faultsubtext'>" + subText+ ".<span>"
                    if (data["0"] == true) {
                      $(row)[0].firstChild.style.textAlign = "center";
                      $(row)[0].firstChild.innerHTML = "<img class=”iconcellsvg” src='/module/fddModule/rc/img/Alarm.svg'/>"
                      $(row).addClass("warning");

                    }else{
                       $(row)[0].firstChild.style.textAlign = "center";
                       $(row)[0].firstChild.innerHTML = "<img class=”iconcellsvg” src='/module/fddModule/rc/img/Off.svg'/>"
                    }

                    //if (data["2"] == true) {
                    //}
                  },
                //responsive: true
             });
              table.buttons().container().appendTo( '#example_wrapper .col-md-6:eq(0)' );
       }

        FDDWidget.prototype.doLoad = function(comp){
           var that = this;
           comp.invoke({
              slot: "getFddData"
           }).then(function(result){
                var jsonObj = JSON.parse(result.get("data"))
                render(jsonObj, that.jq())
           });
        };

        const objDescription = {"CALCULATED SETPOINT ERROR":"VT flow temperature is greater than OAT by a value of the calculated setpoint (when OAT hold off is active)."
        , "COOLING PLANT NOT REQUIRED":"Cooling not required as OAT/water temperature is below agreed upon levels."
        , "COOLING VALVE LOSS OF CONTROL":"Output at 100% for duration longer than expected."
        ,"COOLING VALVE PASSING":"While valve is off, supply temperature is outside of agreed upon levels."
        ,"EXCESS SPEED":"Fan inconsistent with static pressure."
        ,"EXCESSIVE OCCUPIED HOURS":"Building occupied outside of intended occupation times."
        ,"FAN SPEED LOSS OF CONTROL":"Output at 100% for duration longer than expected."
        ,"FANS LOSS OF CONTROL":"Output at 100% for duration longer than expected."
        ,"FCU IN OVERRIDE":"FCU Running when not enabled."
        ,"HEATING PLANT NOT REQUIRED":"Heating in operation when OAT or average space temperature is above agreed upon levels."
        ,"HEATING VALVE LOSS OF CONTROL":"Output at 100% for duration longer than expected."
        ,"HEATING VALVE PASSING":"While valve is off, supply temperature is outside of agreed upon levels."
        ,"HIGH FLOW TEMPERATURE":"Flow temperature above agreed upon levels."
        ,"HIGH USAGE":"Outut exceeds predetermind benchmark."
        ,"ISOLATION VALVE FAILURE":"Flow temperature as specified level when valve is closed."
        ,"LOSS OF CONTROL":"Output at 100% for duration longer than expected."
        ,"OCCUPIED SETPOINT ERROR":"Temperature is above or below agreed upon levels while occupied."
        ,"OFF HOURS OPERATION":"Running when occupied."
        ,"OVER PRESSURE":"Pressure above agreed upon levels."
        ,"OVER TEMPERATURE":"Temperature above agreed upon levels."
        ,"OVERCOOLING":"Cooling enabled when setpoint has already been reached."
        ,"OVERHEATING":"Heating enabled when setpoint has already been reached."
        ,"PLANT IN HAND":"Plant running when not enabled."
        ,"PLANT IN OVERRIDE":"Control valve operating when not enabled."
        ,"PUMP NOT REQUIRED":"Running when there is no demand."
        ,"REDUCED DEADBAND":"Deadband fallen below agreed upon levels."
        ,"SETPOINT ERROR":"Setpoint outside of agreed upon levels."
        ,"SIMULTANEOUS HEATING AND COOLING":"Both cooling and heating in operation (unless de-humidifying)."
        ,"SOFTWARE ERROR":"Hunting or not achieving setpoint."
        ,"SPRAY PUMP LOSS OF CONTROL":"Output at 100% for duration longer than expected."
        ,"SUSPECTED LEAK":"Leak alarm or unit running excessively."
        ,"UNDER PRESSURE":"Pressure below agreed upon levels."
        ,"UNOCCUPIED SETPOINT ERROR":"Temperature is above or below agreed upon levels while unoccupied."
        ,"VALVE FAILURE":"Tank temperature is above setpoint when valve is closed."
        ,"OFF TEST": "Tank temperature is above setpoint when valve is closed."
        };

    return FDDWidget;
});