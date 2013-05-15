

/* First created by JCasGen Sun Nov 04 01:20:39 IST 2012 */
package com.cognizant.dml.text.annotator.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringArray;
import org.cleartk.ne.type.NamedEntityMention;


/** 
 * Updated by JCasGen Sun Nov 04 17:03:59 IST 2012
 * XML source: /usr/home/praveen/workspace/text-annotator-rest-post/src/com/cognizant/dml/text/annotator/type/DBPediaResourceMention.xml
 * @generated */
public class DBPediaResourceMention extends NamedEntityMention {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(DBPediaResourceMention.class);
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
  protected DBPediaResourceMention() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public DBPediaResourceMention(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public DBPediaResourceMention(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public DBPediaResourceMention(JCas jcas, int begin, int end) {
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
  //* Feature: URI

  /** getter for URI - gets 
   * @generated */
  public String getURI() {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_URI == null)
      jcasType.jcas.throwFeatMissing("URI", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_URI);}
    
  /** setter for URI - sets  
   * @generated */
  public void setURI(String v) {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_URI == null)
      jcasType.jcas.throwFeatMissing("URI", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    jcasType.ll_cas.ll_setStringValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_URI, v);}    
   
    
  //*--------------*
  //* Feature: prominence

  /** getter for prominence - gets 
   * @generated */
  public String getProminence() {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_prominence == null)
      jcasType.jcas.throwFeatMissing("prominence", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_prominence);}
    
  /** setter for prominence - sets  
   * @generated */
  public void setProminence(String v) {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_prominence == null)
      jcasType.jcas.throwFeatMissing("prominence", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    jcasType.ll_cas.ll_setStringValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_prominence, v);}    
   
    
  //*--------------*
  //* Feature: resourceType

  /** getter for resourceType - gets 
   * @generated */
  public String getResourceType() {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_resourceType == null)
      jcasType.jcas.throwFeatMissing("resourceType", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_resourceType);}
    
  /** setter for resourceType - sets  
   * @generated */
  public void setResourceType(String v) {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_resourceType == null)
      jcasType.jcas.throwFeatMissing("resourceType", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    jcasType.ll_cas.ll_setStringValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_resourceType, v);}    
   
    
  //*--------------*
  //* Feature: surfaceForm

  /** getter for surfaceForm - gets 
   * @generated */
  public String getSurfaceForm() {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_surfaceForm == null)
      jcasType.jcas.throwFeatMissing("surfaceForm", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_surfaceForm);}
    
  /** setter for surfaceForm - sets  
   * @generated */
  public void setSurfaceForm(String v) {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_surfaceForm == null)
      jcasType.jcas.throwFeatMissing("surfaceForm", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    jcasType.ll_cas.ll_setStringValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_surfaceForm, v);}    
   
    
  //*--------------*
  //* Feature: offset

  /** getter for offset - gets 
   * @generated */
  public long getOffset() {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_offset == null)
      jcasType.jcas.throwFeatMissing("offset", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return jcasType.ll_cas.ll_getLongValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_offset);}
    
  /** setter for offset - sets  
   * @generated */
  public void setOffset(long v) {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_offset == null)
      jcasType.jcas.throwFeatMissing("offset", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    jcasType.ll_cas.ll_setLongValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_offset, v);}    
   
    
  //*--------------*
  //* Feature: similarityScore

  /** getter for similarityScore - gets 
   * @generated */
  public double getSimilarityScore() {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_similarityScore == null)
      jcasType.jcas.throwFeatMissing("similarityScore", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_similarityScore);}
    
  /** setter for similarityScore - sets  
   * @generated */
  public void setSimilarityScore(double v) {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_similarityScore == null)
      jcasType.jcas.throwFeatMissing("similarityScore", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_similarityScore, v);}    
   
    
  //*--------------*
  //* Feature: percentageOfSecondRank

  /** getter for percentageOfSecondRank - gets 
   * @generated */
  public double getPercentageOfSecondRank() {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_percentageOfSecondRank == null)
      jcasType.jcas.throwFeatMissing("percentageOfSecondRank", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_percentageOfSecondRank);}
    
  /** setter for percentageOfSecondRank - sets  
   * @generated */
  public void setPercentageOfSecondRank(double v) {
    if (DBPediaResourceMention_Type.featOkTst && ((DBPediaResourceMention_Type)jcasType).casFeat_percentageOfSecondRank == null)
      jcasType.jcas.throwFeatMissing("percentageOfSecondRank", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((DBPediaResourceMention_Type)jcasType).casFeatCode_percentageOfSecondRank, v);}    
  }

    