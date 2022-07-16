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
                paging: false,
                scrollCollapse: false,
                autoWidth: false,
                scrollX: true,
                data: jsonObj,
                buttons: [ 'csv', 'copy', 'colvis' ],
                columns: [
                        { title: "Asset Name" },
                        { title: "Fault Name" },
                        { title: "Is Active" },
                        { title: "Act Time" },
                        { title: "De-Act Time" },
                        { title: "Duration" },
                        { title: "Cost Annualised" },
                        { title: "Kwh" },
                        { title: "Co2" }
                ]
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

    return FDDWidget;
});