
/* First created by JCasGen Thu Oct 11 19:32:00 IST 2012 */
package com.cognizant.dml.text.annotator.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

import org.apache.uima.jcas.cas.TOP_Type;

/** 
 * Updated by JCasGen Fri Oct 12 00:02:52 IST 2012
 * @generated */
public class Markup_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Markup_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Markup_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Markup(addr, Markup_Type.this);
  			   Markup_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Markup(addr, Markup_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Markup.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.cognizant.dml.text.Markup");



  /** @generated */
  final Feature casFeat_name;
  /** @generated */
  final int     casFeatCode_name;
  /** @generated */ 
  public String getName(int addr) {
        if (featOkTst && casFeat_name == null)
      jcas.throwFeatMissing("name", "com.cognizant.dml.text.Markup");
    return ll_cas.ll_getStringValue(addr, casFeatCode_name);
  }
  /** @generated */    
  public void setName(int addr, String v) {
        if (featOkTst && casFeat_name == null)
      jcas.throwFeatMissing("name", "com.cognizant.dml.text.Markup");
    ll_cas.ll_setStringValue(addr, casFeatCode_name, v);}
    
  
 
  /** @generated */
  final Feature casFeat_attributes;
  /** @generated */
  final int     casFeatCode_attributes;
  /** @generated */ 
  public int getAttributes(int addr) {
        if (featOkTst && casFeat_attributes == null)
      jcas.throwFeatMissing("attributes", "com.cognizant.dml.text.Markup");
    return ll_cas.ll_getRefValue(addr, casFeatCode_attributes);
  }
  /** @generated */    
  public void setAttributes(int addr, int v) {
        if (featOkTst && casFeat_attributes == null)
      jcas.throwFeatMissing("attributes", "com.cognizant.dml.text.Markup");
    ll_cas.ll_setRefValue(addr, casFeatCode_attributes, v);}
    
   /** @generated */
  public int getAttributes(int addr, int i) {
        if (featOkTst && casFeat_attributes == null)
      jcas.throwFeatMissing("attributes", "com.cognizant.dml.text.Markup");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_attributes), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_attributes), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_attributes), i);
  }
   
  /** @generated */ 
  public void setAttributes(int addr, int i, int v) {
        if (featOkTst && casFeat_attributes == null)
      jcas.throwFeatMissing("attributes", "com.cognizant.dml.text.Markup");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_attributes), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_attributes), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_attributes), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_isStart;
  /** @generated */
  final int     casFeatCode_isStart;
  /** @generated */ 
  public boolean getIsStart(int addr) {
        if (featOkTst && casFeat_isStart == null)
      jcas.throwFeatMissing("isStart", "com.cognizant.dml.text.Markup");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_isStart);
  }
  /** @generated */    
  public void setIsStart(int addr, boolean v) {
        if (featOkTst && casFeat_isStart == null)
      jcas.throwFeatMissing("isStart", "com.cognizant.dml.text.Markup");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_isStart, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Markup_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_name = jcas.getRequiredFeatureDE(casType, "name", "uima.cas.String", featOkTst);
    casFeatCode_name  = (null == casFeat_name) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_name).getCode();

 
    casFeat_attributes = jcas.getRequiredFeatureDE(casType, "attributes", "uima.cas.FSArray", featOkTst);
    casFeatCode_attributes  = (null == casFeat_attributes) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_attributes).getCode();

 
    casFeat_isStart = jcas.getRequiredFeatureDE(casType, "isStart", "uima.cas.Boolean", featOkTst);
    casFeatCode_isStart  = (null == casFeat_isStart) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_isStart).getCode();

  }
}



    