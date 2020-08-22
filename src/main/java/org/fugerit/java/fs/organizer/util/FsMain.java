package org.fugerit.java.fs.organizer.util;

import java.util.Properties;

import org.fugerit.java.core.cli.ArgUtils;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.PropsIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FsMain {
    
    private static final Logger logger = LoggerFactory.getLogger( FsMain.class );

    public static final String PARAM_RULE = "rule";

    public static void main(String[] args) throws Exception {
        logger.info( FsMain.class.getSimpleName()+" START" );
        Properties params = ArgUtils.getArgs(args, true);
        String ruleId = params.getProperty( PARAM_RULE );
        if ( StringUtils.isEmpty( ruleId ) ) {
        	throw new Exception( "Param "+PARAM_RULE+" is required!" );
        } else {
        	Properties rules = PropsIO.loadFromClassLoader( "config/rules.properties" );
        	String ruleType= rules.getProperty( ruleId, ruleId );
        	logger.info( "ruleId : {} -> ruleType : {}", ruleId, ruleType );
        	RuleBase rule = (RuleBase)ClassHelper.newInstance( ruleType );
        	int res = rule.apply( params );
        	logger.info( "result -> {}", res );
        }
        logger.info( FsMain.class.getSimpleName()+" END" );
    }

}