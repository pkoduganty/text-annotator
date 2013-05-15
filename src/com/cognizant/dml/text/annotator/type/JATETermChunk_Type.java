
/* First created by JCasGen Sun Dec 09 22:36:33 IST 2012 */
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
import org.cleartk.ne.type.Chunk_Type;

/** 
 * Updated by JCasGen Mon Dec 10 11:32:33 IST 2012
 * @generated */
public class JATETermChunk_Type extends Chunk_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (JATETermChunk_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = JATETermChunk_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new JATETermChunk(addr, JATETermChunk_Type.this);
  			   JATETermChunk_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new JATETermChunk(addr, JATETermChunk_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JATETermChunk.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.cognizant.dml.text.annotator.type.JATETermChunk");
 
  /** @generated */
  final Feature casFeat_tfidf;
  /** @generated */
  final int     casFeatCode_tfidf;
  /** @generated */ 
  public double getTfidf(int addr) {
        if (featOkTst && casFeat_tfidf == null)
      jcas.throwFeatMissing("tfidf", "com.cognizant.dml.text.annotator.type.JATETermChunk");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_tfidf);
  }
  /** @generated */    
  public void setTfidf(int addr, double v) {
        if (featOkTst && casFeat_tfidf == null)
      jcas.throwFeatMissing("tfidf", "com.cognizant.dml.text.annotator.type.JATETermChunk");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_tfidf, v);}
    
  
 
  /** @generated */
  final Feature casFeat_cvalue;
  /** @generated */
  final int     casFeatCode_cvalue;
  /** @generated */ 
  public double getCvalue(int addr) {
        if (featOkTst && casFeat_cvalue == null)
      jcas.throwFeatMissing("cvalue", "com.cognizant.dml.text.annotator.type.JATETermChunk");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_cvalue);
  }
  /** @generated */    
  public void setCvalue(int addr, double v) {
        if (featOkTst && casFeat_cvalue == null)
      jcas.throwFeatMissing("cvalue", "com.cognizant.dml.text.annotator.type.JATETermChunk");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_cvalue, v);}
    
  
 
  /** @generated */
  final Feature casFeat_termex;
  /** @generated */
  final int     casFeatCode_termex;
  /** @generated */ 
  public double getTermex(int addr) {
        if (featOkTst && casFeat_termex == null)
      jcas.throwFeatMissing("termex", "com.cognizant.dml.text.annotator.type.JATETermChunk");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_termex);
  }
  /** @generated */    
  public void setTermex(int addr, double v) {
        if (featOkTst && casFeat_termex == null)
      jcas.throwFeatMissing("termex", "com.cognizant.dml.text.annotator.type.JATETermChunk");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_termex, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public JATETermChunk_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_tfidf = jcas.getRequiredFeatureDE(casType, "tfidf", "uima.cas.Double", featOkTst);
    casFeatCode_tfidf  = (null == casFeat_tfidf) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_tfidf).getCode();

 
    casFeat_cvalue = jcas.getRequiredFeatureDE(casType, "cvalue", "uima.cas.Double", featOkTst);
    casFeatCode_cvalue  = (null == casFeat_cvalue) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_cvalue).getCode();

 
    casFeat_termex = jcas.getRequiredFeatureDE(casType, "termex", "uima.cas.Double", featOkTst);
    casFeatCode_termex  = (null == casFeat_termex) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_termex).getCode();

  }
}



    