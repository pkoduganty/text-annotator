

/* First created by JCasGen Thu Oct 11 19:32:00 IST 2012 */
package com.cognizant.dml.text.annotator.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Oct 12 00:02:52 IST 2012
 * XML source: /usr/home/praveen/workspace/text-annotator-rest-post/src/com/cognizant/dml/text/TypeSystem.xml
 * @generated */
public class MarkupAttribute extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(MarkupAttribute.class);
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
  protected MarkupAttribute() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public MarkupAttribute(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public MarkupAttribute(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public MarkupAttribute(JCas jcas, int begin, int end) {
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
    if (MarkupAttribute_Type.featOkTst && ((MarkupAttribute_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "com.cognizant.dml.text.MarkupAttribute");
    return jcasType.ll_cas.ll_getStringValue(addr, ((MarkupAttribute_Type)jcasType).casFeatCode_name);}
    
  /** setter for name - sets  
   * @generated */
  public void setName(String v) {
    if (MarkupAttribute_Type.featOkTst && ((MarkupAttribute_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "com.cognizant.dml.text.MarkupAttribute");
    jcasType.ll_cas.ll_setStringValue(addr, ((MarkupAttribute_Type)jcasType).casFeatCode_name, v);}    
   
    
  //*--------------*
  //* Feature: value

  /** getter for value - gets 
   * @generated */
  public String getValue() {
    if (MarkupAttribute_Type.featOkTst && ((MarkupAttribute_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "com.cognizant.dml.text.MarkupAttribute");
    return jcasType.ll_cas.ll_getStringValue(addr, ((MarkupAttribute_Type)jcasType).casFeatCode_value);}
    
  /** setter for value - sets  
   * @generated */
  public void setValue(String v) {
    if (MarkupAttribute_Type.featOkTst && ((MarkupAttribute_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "com.cognizant.dml.text.MarkupAttribute");
    jcasType.ll_cas.ll_setStringValue(addr, ((MarkupAttribute_Type)jcasType).casFeatCode_value, v);}    
  }

    