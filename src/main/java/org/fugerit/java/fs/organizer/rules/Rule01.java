package org.fugerit.java.fs.organizer.rules;

import java.io.File;
import java.util.Properties;

import org.fugerit.java.fs.organizer.util.RuleBase;

public class Rule01 extends RuleBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6798594326257416688L;
	
	public static final String PARAM_BASE_DIR = "base-dir";
	public static final String PARAM_IN_DIR = "input";
	public static final String PARAM_OUT_DIR = "output";
	
	@Override
	public int apply(Properties params) throws Exception {
		int res = DEFAULT_OK;
        logger.info( this.getClass().getSimpleName()+" START" );
        String paramBase = params.getProperty( PARAM_BASE_DIR );
        String paramIn = params.getProperty( PARAM_IN_DIR, "" );
        String paramOut = params.getProperty( PARAM_OUT_DIR, "" );
        logger.info( "paramBase -> {}", paramBase );
        logger.info( "paramIn -> {}", paramIn );
        logger.info( "paramOut -> {}", paramOut );
        File baseDir = new File( paramBase );
        File in = new File( baseDir, paramIn );
        File out = new File( baseDir, paramOut );
        logger.info( "in  -> "+in );
        for ( File currentIn : in.listFiles() ) {
            File part1 = currentIn.listFiles()[0];
            File part2 = part1.listFiles()[0];
            String outName = part1.getName()+" - "+part2.getName();
            outName = outName.toLowerCase().replaceAll( " " , "_" );
            File outFile = new File( out, outName );
            logger.info( "{} -> {}", part2.getCanonicalPath(), outFile.getCanonicalPath() );
            part2.renameTo( outFile );
            part1.delete();
            currentIn.delete();
        }
        logger.info( "out -> "+out );
        logger.info( this.getClass().getSimpleName()+" END" );
		return res;
	}

}