
/* First created by JCasGen Sun Nov 04 01:20:39 IST 2012 */
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
import org.cleartk.ne.type.NamedEntityMention_Type;

/** 
 * Updated by JCasGen Sun Nov 04 17:03:59 IST 2012
 * @generated */
public class DBPediaResourceMention_Type extends NamedEntityMention_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (DBPediaResourceMention_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = DBPediaResourceMention_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new DBPediaResourceMention(addr, DBPediaResourceMention_Type.this);
  			   DBPediaResourceMention_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new DBPediaResourceMention(addr, DBPediaResourceMention_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = DBPediaResourceMention.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
 
  /** @generated */
  final Feature casFeat_URI;
  /** @generated */
  final int     casFeatCode_URI;
  /** @generated */ 
  public String getURI(int addr) {
        if (featOkTst && casFeat_URI == null)
      jcas.throwFeatMissing("URI", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return ll_cas.ll_getStringValue(addr, casFeatCode_URI);
  }
  /** @generated */    
  public void setURI(int addr, String v) {
        if (featOkTst && casFeat_URI == null)
      jcas.throwFeatMissing("URI", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    ll_cas.ll_setStringValue(addr, casFeatCode_URI, v);}
    
  
 
  /** @generated */
  final Feature casFeat_prominence;
  /** @generated */
  final int     casFeatCode_prominence;
  /** @generated */ 
  public String getProminence(int addr) {
        if (featOkTst && casFeat_prominence == null)
      jcas.throwFeatMissing("prominence", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return ll_cas.ll_getStringValue(addr, casFeatCode_prominence);
  }
  /** @generated */    
  public void setProminence(int addr, String v) {
        if (featOkTst && casFeat_prominence == null)
      jcas.throwFeatMissing("prominence", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    ll_cas.ll_setStringValue(addr, casFeatCode_prominence, v);}
    
  
 
  /** @generated */
  final Feature casFeat_resourceType;
  /** @generated */
  final int     casFeatCode_resourceType;
  /** @generated */ 
  public String getResourceType(int addr) {
        if (featOkTst && casFeat_resourceType == null)
      jcas.throwFeatMissing("resourceType", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return ll_cas.ll_getStringValue(addr, casFeatCode_resourceType);
  }
  /** @generated */    
  public void setResourceType(int addr, String v) {
        if (featOkTst && casFeat_resourceType == null)
      jcas.throwFeatMissing("resourceType", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    ll_cas.ll_setStringValue(addr, casFeatCode_resourceType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_surfaceForm;
  /** @generated */
  final int     casFeatCode_surfaceForm;
  /** @generated */ 
  public String getSurfaceForm(int addr) {
        if (featOkTst && casFeat_surfaceForm == null)
      jcas.throwFeatMissing("surfaceForm", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return ll_cas.ll_getStringValue(addr, casFeatCode_surfaceForm);
  }
  /** @generated */    
  public void setSurfaceForm(int addr, String v) {
        if (featOkTst && casFeat_surfaceForm == null)
      jcas.throwFeatMissing("surfaceForm", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    ll_cas.ll_setStringValue(addr, casFeatCode_surfaceForm, v);}
    
  
 
  /** @generated */
  final Feature casFeat_offset;
  /** @generated */
  final int     casFeatCode_offset;
  /** @generated */ 
  public long getOffset(int addr) {
        if (featOkTst && casFeat_offset == null)
      jcas.throwFeatMissing("offset", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return ll_cas.ll_getLongValue(addr, casFeatCode_offset);
  }
  /** @generated */    
  public void setOffset(int addr, long v) {
        if (featOkTst && casFeat_offset == null)
      jcas.throwFeatMissing("offset", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    ll_cas.ll_setLongValue(addr, casFeatCode_offset, v);}
    
  
 
  /** @generated */
  final Feature casFeat_similarityScore;
  /** @generated */
  final int     casFeatCode_similarityScore;
  /** @generated */ 
  public double getSimilarityScore(int addr) {
        if (featOkTst && casFeat_similarityScore == null)
      jcas.throwFeatMissing("similarityScore", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_similarityScore);
  }
  /** @generated */    
  public void setSimilarityScore(int addr, double v) {
        if (featOkTst && casFeat_similarityScore == null)
      jcas.throwFeatMissing("similarityScore", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_similarityScore, v);}
    
  
 
  /** @generated */
  final Feature casFeat_percentageOfSecondRank;
  /** @generated */
  final int     casFeatCode_percentageOfSecondRank;
  /** @generated */ 
  public double getPercentageOfSecondRank(int addr) {
        if (featOkTst && casFeat_percentageOfSecondRank == null)
      jcas.throwFeatMissing("percentageOfSecondRank", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_percentageOfSecondRank);
  }
  /** @generated */    
  public void setPercentageOfSecondRank(int addr, double v) {
        if (featOkTst && casFeat_percentageOfSecondRank == null)
      jcas.throwFeatMissing("percentageOfSecondRank", "com.cognizant.dml.text.annotator.type.DBPediaResourceMention");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_percentageOfSecondRank, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public DBPediaResourceMention_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_URI = jcas.getRequiredFeatureDE(casType, "URI", "uima.cas.String", featOkTst);
    casFeatCode_URI  = (null == casFeat_URI) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_URI).getCode();

 
    casFeat_prominence = jcas.getRequiredFeatureDE(casType, "prominence", "uima.cas.String", featOkTst);
    casFeatCode_prominence  = (null == casFeat_prominence) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_prominence).getCode();

 
    casFeat_resourceType = jcas.getRequiredFeatureDE(casType, "resourceType", "uima.cas.String", featOkTst);
    casFeatCode_resourceType  = (null == casFeat_resourceType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_resourceType).getCode();

 
    casFeat_surfaceForm = jcas.getRequiredFeatureDE(casType, "surfaceForm", "uima.cas.String", featOkTst);
    casFeatCode_surfaceForm  = (null == casFeat_surfaceForm) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_surfaceForm).getCode();

 
    casFeat_offset = jcas.getRequiredFeatureDE(casType, "offset", "uima.cas.Long", featOkTst);
    casFeatCode_offset  = (null == casFeat_offset) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_offset).getCode();

 
    casFeat_similarityScore = jcas.getRequiredFeatureDE(casType, "similarityScore", "uima.cas.Double", featOkTst);
    casFeatCode_similarityScore  = (null == casFeat_similarityScore) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_similarityScore).getCode();

 
    casFeat_percentageOfSecondRank = jcas.getRequiredFeatureDE(casType, "percentageOfSecondRank", "uima.cas.Double", featOkTst);
    casFeatCode_percentageOfSecondRank  = (null == casFeat_percentageOfSecondRank) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_percentageOfSecondRank).getCode();

  }
}



    