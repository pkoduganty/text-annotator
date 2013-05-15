

/* First created by JCasGen Sat Nov 03 23:57:49 IST 2012 */
package org.cleartk.ne.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Sat Nov 03 23:57:49 IST 2012
 * XML source: /usr/home/praveen/workspace/text-annotator-rest-post/src/com/cognizant/dml/text/annotator/type/DBPediaResource.xml
 * @generated */
public class GazetteerNamedEntityMention extends NamedEntityMention {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(GazetteerNamedEntityMention.class);
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
  protected GazetteerNamedEntityMention() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public GazetteerNamedEntityMention(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public GazetteerNamedEntityMention(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public GazetteerNamedEntityMention(JCas jcas, int begin, int end) {
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
     
}

    