package com.cognizant.dml.text.annotator.util;
import java.util.Comparator;

import org.cleartk.ne.term.util.Term;

/** 
 * Copyright (c) 2007-2008, Regents of the University of Colorado 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 * Neither the name of the University of Colorado at Boulder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
 */


/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 */
public class TypedTermMatch extends org.cleartk.ne.term.util.TermMatch {	
  private String type;

  /**
   * @throws IllegalArgumentException
   *           if end is less than begin, term is null, or the length of the term's text does not
   *           equal the difference of end and begin.
   */
  public TypedTermMatch(int begin, int end, Term term, String type) throws IllegalArgumentException {
    super(begin,end,term);
    this.type=type;
  }
  
  public String getType(){
	  return type;
  }

  public static class TypedTermMatchComparator implements Comparator<TypedTermMatch> {
	@Override
	public int compare(TypedTermMatch o1, TypedTermMatch o2) {
		int bc=new Integer(o1.getBegin()).compareTo(o2.getBegin());
		if(bc==0)
			return new Integer(o1.getEnd()).compareTo(o2.getEnd());
		return bc;
	}		 
  }
}
