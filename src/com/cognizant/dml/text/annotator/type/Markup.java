

/* First created by JCasGen Thu Oct 11 19:32:00 IST 2012 */
package com.cognizant.dml.text.annotator.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Oct 12 00:02:52 IST 2012
 * XML source: /usr/home/praveen/workspace/text-annotator-rest-post/src/com/cognizant/dml/text/TypeSystem.xml
 * @generated */
public class Markup extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Markup.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Markup() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Markup(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Markup(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Markup(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
  //*--------------*
  //* Feature: name

  /** getter for name - gets 
   * @generated */
  public String getName() {
    if (Markup_Type.featOkTst && ((Markup_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "com.cognizant.dml.text.Markup");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Markup_Type)jcasType).casFeatCode_name);}
    
  /** setter for name - sets  
   * @generated */
  public void setName(String v) {
    if (Markup_Type.featOkTst && ((Markup_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "com.cognizant.dml.text.Markup");
    jcasType.ll_cas.ll_setStringValue(addr, ((Markup_Type)jcasType).casFeatCode_name, v);}    
   
    
  //*--------------*
  //* Feature: attributes

  /** getter for attributes - gets 
   * @generated */
  public FSArray getAttributes() {
    if (Markup_Type.featOkTst && ((Markup_Type)jcasType).casFeat_attributes == null)
      jcasType.jcas.throwFeatMissing("attributes", "com.cognizant.dml.text.Markup");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Markup_Type)jcasType).casFeatCode_attributes)));}
    
  /** setter for attributes - sets  
   * @generated */
  public void setAttributes(FSArray v) {
    if (Markup_Type.featOkTst && ((Markup_Type)jcasType).casFeat_attributes == null)
      jcasType.jcas.throwFeatMissing("attributes", "com.cognizant.dml.text.Markup");
    jcasType.ll_cas.ll_setRefValue(addr, ((Markup_Type)jcasType).casFeatCode_attributes, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for attributes - gets an indexed value - 
   * @generated */
  public MarkupAttribute getAttributes(int i) {
    if (Markup_Type.featOkTst && ((Markup_Type)jcasType).casFeat_attributes == null)
      jcasType.jcas.throwFeatMissing("attributes", "com.cognizant.dml.text.Markup");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Markup_Type)jcasType).casFeatCode_attributes), i);
    return (MarkupAttribute)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Markup_Type)jcasType).casFeatCode_attributes), i)));}

  /** indexed setter for attributes - sets an indexed value - 
   * @generated */
  public void setAttributes(int i, MarkupAttribute v) { 
    if (Markup_Type.featOkTst && ((Markup_Type)jcasType).casFeat_attributes == null)
      jcasType.jcas.throwFeatMissing("attributes", "com.cognizant.dml.text.Markup");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Markup_Type)jcasType).casFeatCode_attributes), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Markup_Type)jcasType).casFeatCode_attributes), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: isStart

  /** getter for isStart - gets 
   * @generated */
  public boolean getIsStart() {
    if (Markup_Type.featOkTst && ((Markup_Type)jcasType).casFeat_isStart == null)
      jcasType.jcas.throwFeatMissing("isStart", "com.cognizant.dml.text.Markup");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((Markup_Type)jcasType).casFeatCode_isStart);}
    
  /** setter for isStart - sets  
   * @generated */
  public void setIsStart(boolean v) {
    if (Markup_Type.featOkTst && ((Markup_Type)jcasType).casFeat_isStart == null)
      jcasType.jcas.throwFeatMissing("isStart", "com.cognizant.dml.text.Markup");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((Markup_Type)jcasType).casFeatCode_isStart, v);}    
  }

    