package com.rocketGX.fddModule.ux;

import com.tridium.bql.util.BDynamicTimeRange;
import com.tridium.json.JSONArray;

import javax.baja.collection.BITable;
import javax.baja.control.BBooleanWritable;
import javax.baja.history.*;
import javax.baja.history.db.BHistoryDatabase;
import javax.baja.naming.BOrd;
import javax.baja.naming.SlotPath;
import javax.baja.nre.annotations.NiagaraAction;
import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
import javax.baja.util.Lexicon;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@NiagaraType
//@NiagaraProperty(name = "stationFddOrd", type = "BOrd", defaultValue = "BOrd.NULL", flags = Flags.SUMMARY)
@NiagaraProperty(
        name = "TimeRange",
        type = "BDynamicTimeRange",
        defaultValue = "BDynamicTimeRange.LAST_MONTH"
)
@NiagaraProperty(name = "startDate", type = "BAbsTime", defaultValue = "BAbsTime.DEFAULT", flags = Flags.READONLY)
@NiagaraProperty(name = "endDate", type = "BAbsTime", defaultValue = "BAbsTime.DEFAULT", flags = Flags.READONLY)
@NiagaraAction(name="getFddData", returnType = "BValue",flags = Flags.OPERATOR)
@NiagaraAction(name="addAsset", parameterType = "BString",defaultValue = "BString.DEFAULT",flags = Flags.OPERATOR)
public class BFDDService extends BAbstractService {


/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.rocketGX.fddModule.ux.BFDDService(560574665)1.0$ @*/
/* Generated Sun Jun 12 17:31:27 AEST 2022 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Property "TimeRange"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code TimeRange} property.
   * @see #getTimeRange
   * @see #setTimeRange
   */
  public static final Property TimeRange = newProperty(0, BDynamicTimeRange.LAST_MONTH, null);
  
  /**
   * Get the {@code TimeRange} property.
   * @see #TimeRange
   */
  public BDynamicTimeRange getTimeRange() { return (BDynamicTimeRange)get(TimeRange); }
  
  /**
   * Set the {@code TimeRange} property.
   * @see #TimeRange
   */
  public void setTimeRange(BDynamicTimeRange v) { set(TimeRange, v, null); }

////////////////////////////////////////////////////////////////
// Property "startDate"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code startDate} property.
   * @see #getStartDate
   * @see #setStartDate
   */
  public static final Property startDate = newProperty(Flags.READONLY, BAbsTime.DEFAULT, null);
  
  /**
   * Get the {@code startDate} property.
   * @see #startDate
   */
  public BAbsTime getStartDate() { return (BAbsTime)get(startDate); }
  
  /**
   * Set the {@code startDate} property.
   * @see #startDate
   */
  public void setStartDate(BAbsTime v) { set(startDate, v, null); }

////////////////////////////////////////////////////////////////
// Property "endDate"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code endDate} property.
   * @see #getEndDate
   * @see #setEndDate
   */
  public static final Property endDate = newProperty(Flags.READONLY, BAbsTime.DEFAULT, null);
  
  /**
   * Get the {@code endDate} property.
   * @see #endDate
   */
  public BAbsTime getEndDate() { return (BAbsTime)get(endDate); }
  
  /**
   * Set the {@code endDate} property.
   * @see #endDate
   */
  public void setEndDate(BAbsTime v) { set(endDate, v, null); }

////////////////////////////////////////////////////////////////
// Action "getFddData"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code getFddData} action.
   * @see #getFddData()
   */
  public static final Action getFddData = newAction(Flags.OPERATOR, null);
  
  /**
   * Invoke the {@code getFddData} action.
   * @see #getFddData
   */
  public BValue getFddData() { return (BValue)invoke(getFddData, null, null); }

////////////////////////////////////////////////////////////////
// Action "addAsset"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code addAsset} action.
   * @see #addAsset(BString parameter)
   */
  public static final Action addAsset = newAction(Flags.OPERATOR, BString.DEFAULT, null);
  
  /**
   * Invoke the {@code addAsset} action.
   * @see #addAsset
   */
  public void addAsset(BString parameter) { invoke(addAsset, parameter, null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BFDDService.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private Type[] serviceTypes = new Type[]{this.getType()};
    static final Lexicon lex = Lexicon.make(BFDDService.class);

    public BFDDService() {
    }

    public static BFDDService getService() {
        return (BFDDService)Sys.getService(TYPE);
    }

    public Type[] getServiceTypes() {
        return this.serviceTypes;
    }

    public void doAddAsset(BString assetName) {
        try {
            this.add(SlotPath.escape(assetName.toString()), new BAssetComponent());
        }catch (Exception ex){}
    }

    public BValue doGetFddData(){
        BComponent result = new BComponent();
        try{
            JSONArray dtArray = new JSONArray();
            addFDDData(dtArray);
            result.add("status", BString.make("status"));
            result.add("data",BString.make(dtArray.toString()));
            result.add("message",BString.make("Got the station data"));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public void getHistoryData(String historyName, JSONArray dtArray, String assetName, String faultName, boolean status, double energy, double co2){
        try {
            setStartDate(getTimeRange().getStartTime(BAbsTime.now()));
            setEndDate(getTimeRange().getEndTime(BAbsTime.now()));
            BHistoryDatabase historyDatabase = getHistoryDatabase();
            HistorySpaceConnection conn = historyDatabase.getConnection(null);
            BHistoryId historyId = getHistoryId(historyDatabase, historyName);
            BIHistory runNumerHistory = conn.getHistory(historyId);
            BAbsTime lastTimestamp = conn.getLastTimestamp(runNumerHistory);
            BITable<BHistoryRecord> collection = conn.timeQuery(runNumerHistory, getStartDate(), getEndDate());
            if (collection != null) {
                List<String> areas = new ArrayList<>();
                int count = -1;
                boolean addLastRec = true;
                BAbsTime start = null;
                BAbsTime startChanged = null;
                BAbsTime end = null;
                JSONArray obj = null;
                int h = 0;
                int m = 0;
                int s = 0;
                try (Cursor <BHistoryRecord> cursor = collection.cursor()) {
                    while (cursor.next()) {
                        BHistoryRecord rec = cursor.get();
                        if(rec instanceof  BHistoryRecord){
                            BTrendFlags flags = ((BBooleanTrendRecord)rec).getTrendFlags();
                            boolean value = ((BBooleanTrendRecord) rec).getValue();
                            if(value && count == -1){
                                start = rec.getTimestamp();
                                end = null;
                                count = 0;
                            }

                            if(!value && count == 0){
                                end = rec.getTimestamp();
                                count = -1;
                            }

                            if(start !=null && end !=null){
                                BRelTime duration = start.delta(end);
                                String hrsMinSec = getTimeHMS(duration.getMillis());
                                int hrs = Integer.parseInt(getTimeHMS(duration.getMillis()).split(":")[0]);
                                int min = Integer.parseInt(getTimeHMS(duration.getMillis()).split(":")[1]);
                                int sec = Integer.parseInt(getTimeHMS(duration.getMillis()).split(":")[2]);

                                h = h + hrs;
                                if((m+min)>=60){
                                    h = h + 1;
                                    m = (m+min) - 60;
                                }else {
                                    m = m+min;
                                }

                                if((s+sec) >= 60){
                                    m = m + 1 ;
                                    s = (s+sec)-60;
                                }else {
                                    s = s + sec;
                                }

                            }
                        }

                      /*  if(status && addLastRec){
                            JSONArray objLst = new JSONArray();
                            objLst.put("Running");
                            objLst.put(assetName);
                            objLst.put(faultName);
                            objLst.put( lastTimestamp);
                            objLst.put( "-");
                            objLst.put( "-");
                            objLst.put( "-");
                            objLst.put( "-");
                            objLst.put( "-");
                            dtArray.put(objLst);
                            addLastRec = false;
                        }
                        if (rec instanceof BHistoryRecord) {
                            //System.out.println("Inside history records");
                            BTrendFlags flags = ((BBooleanTrendRecord)rec).getTrendFlags();
                            boolean value = ((BBooleanTrendRecord) rec).getValue();

                            if(count == -1 && value){
                                start = rec.getTimestamp();
                                count ++;
                            }else if(!value && start !=null){
                                end = rec.getTimestamp();
                                BRelTime duration = start.delta(end);

                                if(end != lastTimestamp && startChanged !=null){
                                    if(startChanged == start){
                                        dtArray.remove(dtArray.length()-1);
                                    }
                                }
                                String dur = duration.toString().replace(" days", "d").replace(" hours", "h")
                                        .replace(" minutes", "m").replace(" minute", "m").replace(" seconds", "s");
                                obj = new JSONArray();
                                obj.put("Stopped");
                                obj.put(assetName);
                                obj.put(faultName);
                                obj.put( start.toLocalTime().toString());
                                obj.put( end.toLocalTime().toString());
                                obj.put( dur);
                                obj.put( "-");
                                obj.put( "-");
                                obj.put( "-");
                                dtArray.put(obj);
                                startChanged = start;
                                count = -1;
                            }
                        }*/
                    }

                    String duration = h + ":" + m + ":" + s;
                    obj = new JSONArray();
                    obj.put(status);
                    obj.put(assetName);
                    obj.put(faultName);
                    obj.put( start.toLocalTime().toString());
                    obj.put( end == null? "-" : end.toLocalTime().toString());
                    obj.put( duration);
                    obj.put( "-");
                    obj.put( energy + " KWh");
                    obj.put( co2 + " kgCO2");
                    dtArray.put(obj);
                }
            }
        }catch (Exception ex){

        }
    }

    private String getTimeHMS(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }


    private BHistoryId getHistoryId(BHistoryDatabase historyDatabase, String historyName) {
        BHistoryId historyId = BHistoryId.make(historyDatabase.getDeviceName(),
                SlotPath.escape(historyName));
        return historyId;
    }

    private BHistoryDatabase getHistoryDatabase()
    {
        BHistoryService historyService;
        try
        {
            historyService = (BHistoryService)Sys.getService(BHistoryService.TYPE);
        }
        catch (ServiceNotFoundException e)
        {
            return null;
        }
        return historyService.getDatabase();
    }

    private void addFDDData(JSONArray dtArray) {

       // BFolder alarms = (BFolder) getStationFddOrd().resolve().get();

        Property[] properties = this.getDynamicPropertiesArray();

        for (Property prop : properties)
        {
            if(this.get(prop) instanceof BAssetComponent){
                BAssetComponent comp = (BAssetComponent) this.get(prop);
                double energy = comp.getEnergyCost();
                double co2 = comp.getCo2Factor();
                Property[] ordProperties = comp.getDynamicPropertiesArray();
                for(Property ordProp: ordProperties){
                    if(comp.get(ordProp) instanceof BOrd){
                        BOrd ord = (BOrd) comp.get(ordProp);
                        if(!ord.isNull()){
                            BBooleanWritable fddPoint = (BBooleanWritable) ord.resolve().get();
                            fddPoint.loadSlots();
                            fddPoint.lease();
                            getHistoryData(fddPoint.getName(), dtArray,
                                    comp.getName(), SlotPath.unescape(ordProp.getName()), fddPoint.getOut().getValue(), energy, co2);

                        }
                    }
                }
            }
        }

/*        BFolder AHU_OffHoursOperation = (BFolder) alarms.get("AHU_OffHoursOperation");
        BBooleanWritable ahu_offHrs = (BBooleanWritable) AHU_OffHoursOperation.get("Bool1_OffHoursOperation_Fault");
        getHistoryData("Bool1_OffHoursOperation_Fault", dtArray,
                "AHU", "Off Hours Operation", ahu_offHrs.getOut().getValue());

        BFolder AHU_SimultaneousHtgClg = (BFolder) alarms.get("AHU_SimultaneousHtgClg");
        BBooleanWritable ahu_sim = (BBooleanWritable) AHU_SimultaneousHtgClg.get("Bool2_SimulHtgClg_Fault");
        getHistoryData("Bool2_SimulHtgClg_Fault", dtArray,
                "AHU", "Simultaneous Heating and Cooling",ahu_sim.getOut().getValue());

        BFolder FCU_Overheating = (BFolder) alarms.get("FCU_Overheating");
        BBooleanWritable status = (BBooleanWritable) FCU_Overheating.get("Bool1_Overheating_Fault");
        getHistoryData("Bool1_Overheating_Fault",dtArray,
                "FCU", "Overheating", status.getOut().getValue());

        BFolder FCU_Occupied_Temp_SP = (BFolder) alarms.get("FCU_Occupied_Temp_SP");
        BBooleanWritable fcu_occ = (BBooleanWritable) FCU_Occupied_Temp_SP.get("Bool1_OccTempSP_Fault");
        getHistoryData("Bool1_OccTempSP_Fault", dtArray,
                "FCU", "Occupied Setpoint Error", fcu_occ.getOut().getValue());

        BFolder Chiller_PlantInHand = (BFolder) alarms.get("Chiller_PlantInHand");
        BBooleanWritable chiller_status = (BBooleanWritable) Chiller_PlantInHand.get("Bool1_PlantInHand_Fault");
        getHistoryData("Bool1_PlantInHand_Fault", dtArray,
                "Chiller", "Plant in Hand", chiller_status.getOut().getValue());*/
    }


    @Override
    public void changed(Property property, Context context) {
        super.changed(property, context);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}
