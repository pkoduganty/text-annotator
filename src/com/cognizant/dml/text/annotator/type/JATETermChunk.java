

/* First created by JCasGen Sun Dec 09 22:36:33 IST 2012 */
package com.cognizant.dml.text.annotator.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.cleartk.ne.type.Chunk;


/** 
 * Updated by JCasGen Mon Dec 10 11:32:33 IST 2012
 * XML source: /usr/home/praveen/workspace/text-annotator-rest-post/src/com/cognizant/dml/text/annotator/type/JATETermChunk.xml
 * @generated */
public class JATETermChunk extends Chunk {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(JATETermChunk.class);
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
  protected JATETermChunk() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public JATETermChunk(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public JATETermChunk(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public JATETermChunk(JCas jcas, int begin, int end) {
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
  //* Feature: tfidf

  /** getter for tfidf - gets 
   * @generated */
  public double getTfidf() {
    if (JATETermChunk_Type.featOkTst && ((JATETermChunk_Type)jcasType).casFeat_tfidf == null)
      jcasType.jcas.throwFeatMissing("tfidf", "com.cognizant.dml.text.annotator.type.JATETermChunk");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((JATETermChunk_Type)jcasType).casFeatCode_tfidf);}
    
  /** setter for tfidf - sets  
   * @generated */
  public void setTfidf(double v) {
    if (JATETermChunk_Type.featOkTst && ((JATETermChunk_Type)jcasType).casFeat_tfidf == null)
      jcasType.jcas.throwFeatMissing("tfidf", "com.cognizant.dml.text.annotator.type.JATETermChunk");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((JATETermChunk_Type)jcasType).casFeatCode_tfidf, v);}    
   
    
  //*--------------*
  //* Feature: cvalue

  /** getter for cvalue - gets 
   * @generated */
  public double getCvalue() {
    if (JATETermChunk_Type.featOkTst && ((JATETermChunk_Type)jcasType).casFeat_cvalue == null)
      jcasType.jcas.throwFeatMissing("cvalue", "com.cognizant.dml.text.annotator.type.JATETermChunk");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((JATETermChunk_Type)jcasType).casFeatCode_cvalue);}
    
  /** setter for cvalue - sets  
   * @generated */
  public void setCvalue(double v) {
    if (JATETermChunk_Type.featOkTst && ((JATETermChunk_Type)jcasType).casFeat_cvalue == null)
      jcasType.jcas.throwFeatMissing("cvalue", "com.cognizant.dml.text.annotator.type.JATETermChunk");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((JATETermChunk_Type)jcasType).casFeatCode_cvalue, v);}    
   
    
  //*--------------*
  //* Feature: termex

  /** getter for termex - gets 
   * @generated */
  public double getTermex() {
    if (JATETermChunk_Type.featOkTst && ((JATETermChunk_Type)jcasType).casFeat_termex == null)
      jcasType.jcas.throwFeatMissing("termex", "com.cognizant.dml.text.annotator.type.JATETermChunk");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((JATETermChunk_Type)jcasType).casFeatCode_termex);}
    
  /** setter for termex - sets  
   * @generated */
  public void setTermex(double v) {
    if (JATETermChunk_Type.featOkTst && ((JATETermChunk_Type)jcasType).casFeat_termex == null)
      jcasType.jcas.throwFeatMissing("termex", "com.cognizant.dml.text.annotator.type.JATETermChunk");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((JATETermChunk_Type)jcasType).casFeatCode_termex, v);}    
  }

    