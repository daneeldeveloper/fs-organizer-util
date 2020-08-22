package org.fugerit.java.fs.organizer.util;

import java.io.Serializable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleBase implements Serializable {
	
    /**
	 *
	 */
    private static final long serialVersionUID = -6481624343512528380L;

    protected static final Logger logger = LoggerFactory.getLogger( RuleBase.class );
    
    public static final int DEFAULT_OK = 0;

    public int apply( Properties params ) throws Exception {
        return DEFAULT_OK;
    }

}