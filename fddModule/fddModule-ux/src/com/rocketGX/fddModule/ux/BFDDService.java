package com.rocketGX.fddModule.ux;

import com.tridium.bql.util.BDynamicTimeRange;
import com.tridium.history.db.BLocalDbHistory;
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
import javax.baja.util.BFolder;
import javax.baja.util.Lexicon;
import java.util.ArrayList;
import java.util.List;

@NiagaraType
@NiagaraProperty(name = "stationFddOrd", type = "BOrd", defaultValue = "BOrd.NULL", flags = Flags.SUMMARY)
@NiagaraProperty(
        name = "TimeRange",
        type = "BDynamicTimeRange",
        defaultValue = "BDynamicTimeRange.LAST_MONTH"
)
@NiagaraProperty(name = "startDate", type = "BAbsTime", defaultValue = "BAbsTime.DEFAULT", flags = Flags.READONLY)
@NiagaraProperty(name = "endDate", type = "BAbsTime", defaultValue = "BAbsTime.DEFAULT", flags = Flags.READONLY)
@NiagaraAction(name="getFddData", returnType = "BValue",flags = Flags.OPERATOR)
public class BFDDService extends BAbstractService {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.rocketGX.fddModule.ux.BFDDService(1938931094)1.0$ @*/
/* Generated Sun May 15 13:48:33 AWST 2022 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Property "stationFddOrd"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code stationFddOrd} property.
   * @see #getStationFddOrd
   * @see #setStationFddOrd
   */
  public static final Property stationFddOrd = newProperty(Flags.SUMMARY, BOrd.NULL, null);
  
  /**
   * Get the {@code stationFddOrd} property.
   * @see #stationFddOrd
   */
  public BOrd getStationFddOrd() { return (BOrd)get(stationFddOrd); }
  
  /**
   * Set the {@code stationFddOrd} property.
   * @see #stationFddOrd
   */
  public void setStationFddOrd(BOrd v) { set(stationFddOrd, v, null); }

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

    public void getHistoryData(String historyName, JSONArray dtArray, String assetName, String faultName, boolean status){
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
                BAbsTime end;
                JSONArray obj = null;
                try (Cursor <BHistoryRecord> cursor = collection.cursor()) {
                    while (cursor.next()) {
                        BHistoryRecord rec = cursor.get();

                        if(status && addLastRec){
                            JSONArray objLst = new JSONArray();
                            objLst.put(assetName);
                            objLst.put(faultName);
                            objLst.put("Active");
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
                                obj.put(assetName);
                                obj.put(faultName);
                                obj.put("InActive");
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
                        }
                    }
                }
            }
        }catch (Exception ex){

        }
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

        BFolder alarms = (BFolder) getStationFddOrd().resolve().get();

        BFolder AHU_OffHoursOperation = (BFolder) alarms.get("AHU_OffHoursOperation");
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
                "Chiller", "Plant in Hand", chiller_status.getOut().getValue());
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
