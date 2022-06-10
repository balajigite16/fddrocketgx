package com.rocketGX.fddModule.ux;
import javax.baja.naming.BOrd;
import javax.baja.naming.SlotPath;
import javax.baja.nre.annotations.NiagaraAction;
import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;

@NiagaraType
@NiagaraProperty(name="EnergyCost", type="double", defaultValue = "0.0")
@NiagaraProperty(name="Co2Factor", type="double", defaultValue = "0.0")
@NiagaraAction(name="addFDDPointOrd", parameterType = "BString",defaultValue = "BString.DEFAULT",flags = Flags.OPERATOR)
public class BAssetComponent extends BComponent {

/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.rocketGX.fddModule.ux.BAssetComponent(1967449389)1.0$ @*/
/* Generated Fri Jun 10 17:51:49 AEST 2022 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Property "EnergyCost"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code EnergyCost} property.
   * @see #getEnergyCost
   * @see #setEnergyCost
   */
  public static final Property EnergyCost = newProperty(0, 0.0, null);
  
  /**
   * Get the {@code EnergyCost} property.
   * @see #EnergyCost
   */
  public double getEnergyCost() { return getDouble(EnergyCost); }
  
  /**
   * Set the {@code EnergyCost} property.
   * @see #EnergyCost
   */
  public void setEnergyCost(double v) { setDouble(EnergyCost, v, null); }

////////////////////////////////////////////////////////////////
// Property "Co2Factor"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code Co2Factor} property.
   * @see #getCo2Factor
   * @see #setCo2Factor
   */
  public static final Property Co2Factor = newProperty(0, 0.0, null);
  
  /**
   * Get the {@code Co2Factor} property.
   * @see #Co2Factor
   */
  public double getCo2Factor() { return getDouble(Co2Factor); }
  
  /**
   * Set the {@code Co2Factor} property.
   * @see #Co2Factor
   */
  public void setCo2Factor(double v) { setDouble(Co2Factor, v, null); }

////////////////////////////////////////////////////////////////
// Action "addFDDPointOrd"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code addFDDPointOrd} action.
   * @see #addFDDPointOrd(BString parameter)
   */
  public static final Action addFDDPointOrd = newAction(Flags.OPERATOR, BString.DEFAULT, null);
  
  /**
   * Invoke the {@code addFDDPointOrd} action.
   * @see #addFDDPointOrd
   */
  public void addFDDPointOrd(BString parameter) { invoke(addFDDPointOrd, parameter, null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAssetComponent.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  public void doAddFDDPointOrd(BString ordName) {
    try {
      this.add(SlotPath.escape(ordName.toString()), BOrd.DEFAULT);
    }catch (Exception ex){}
  }
}
