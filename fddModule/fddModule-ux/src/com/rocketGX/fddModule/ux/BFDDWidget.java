package com.rocketGX.fddModule.ux;

import com.tridium.web.BICollectionSupport;

import javax.baja.naming.BOrd;
import javax.baja.nre.annotations.NiagaraSingleton;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.BSingleton;
import javax.baja.sys.Context;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.web.BIFormFactorMax;
import javax.baja.web.BIOffline;
import javax.baja.web.js.BIJavaScript;
import javax.baja.web.js.JsInfo;

@NiagaraSingleton
public final class BFDDWidget extends BSingleton
        implements BIJavaScript,BIFormFactorMax,BIOffline, BICollectionSupport
{

  public static final BFDDWidget INSTANCE = new BFDDWidget();

  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BFDDWidget.class);

  /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  @Override
  public JsInfo getJsInfo(Context context)
  {
    return JS_INFO;
  }
  private static final JsInfo JS_INFO = JsInfo.make(BOrd.make("module://fddModule/rc/FDDWidget.js"));
}
