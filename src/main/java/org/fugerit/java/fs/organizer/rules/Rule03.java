package org.fugerit.java.fs.organizer.rules;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.fugerit.java.fs.organizer.util.RuleBase;

public class Rule03 extends RuleBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6798594326257416688L;
	
	public static final String PARAM_BASE_DIR = "base-dir";
	
	public static int normalizeFile( File currentIn ) throws IOException {
		int totalRename = 0;
		if ( currentIn.isDirectory() ) {
			File[] list = currentIn.listFiles();
			for ( File current : list ) {
				totalRename+= normalizeFile( current );
			}
		}
		String fileName = currentIn.getName();
        fileName = fileName.replaceAll( "'" , "_" );
        fileName = fileName.replace( '&' , '-' );
        fileName = fileName.replace( '?' , '-' );
        fileName = fileName.replace( '.' , '-' );
        fileName = fileName.replace( '(' , '-' );
        fileName = fileName.replace( ')' , '-' );
        fileName = fileName.replace( '[' , '-' );
        fileName = fileName.replace( ']' , '-' );
        fileName = fileName.replace( '(' , '-' );
        fileName = fileName.replace( ':' , '-' );
        fileName = fileName.replace( '"' , '_' );
        fileName = fileName.replace( '*' , '_' );
        fileName = fileName.replace( '>' , '_' );
        fileName = fileName.replaceAll( "\"" , "_" );
        fileName = fileName.replaceAll( "-mp3" , ".mp3" );
        File currentOut = new File( currentIn.getParentFile(), fileName );
        logger.info( "Try {} to {}", currentIn, currentOut );
        if ( !currentIn.getCanonicalPath().contentEquals( currentOut.getCanonicalPath() ) ) {
        	if ( !currentIn.renameTo( currentOut ) ) {
            	logger.info( "Cannot rename {} to {}", currentIn, currentOut );
            } else {
            	totalRename++;
            }
        }
		return totalRename;
	}
	
	@Override
	public int apply(Properties params) throws Exception {
		int res = DEFAULT_OK;
        logger.info( this.getClass().getSimpleName()+" START" );
        String paramBase = params.getProperty( PARAM_BASE_DIR );
        logger.info( "paramBase -> {}", paramBase );
        File baseDir = new File( paramBase );
        int totalRename = normalizeFile( baseDir );
        logger.info( "total rename {}", totalRename );
        logger.info( this.getClass().getSimpleName()+" END" );
		return res;
	}

}