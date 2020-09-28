package org.fugerit.java.fs.organizer.rules;

import java.io.File;
import java.util.Properties;

import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.fs.organizer.util.RuleBase;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

public class Rule02 extends RuleBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6798594326257416688L;
	
	public static final String PARAM_BASE_DIR = "base-dir";
	public static final String PARAM_MOVE = "move";
	public static final String PARAM_OUT_DIR = "output";
	public static final String PARAM_SKIP_ERROR = "skip-error";
	
	@Override
	public int apply(Properties params) throws Exception {
		int res = DEFAULT_OK;
        logger.info( this.getClass().getSimpleName()+" START" );
        String paramBase = params.getProperty( PARAM_BASE_DIR );
        String paramMove = params.getProperty( PARAM_MOVE, "" );
        String paramSkipError = params.getProperty( PARAM_SKIP_ERROR, "" );
        String paramOut = params.getProperty( PARAM_OUT_DIR, "" );
        logger.info( "paramBase -> {}", paramBase );
        logger.info( "paramMove -> {}", paramMove );
        logger.info( "paramSkipError -> {}", paramSkipError );
        logger.info( "paramOut -> {}", paramOut );
        File baseFile = new File( paramBase );
        File[] listFile = baseFile.listFiles();
        boolean doMove = BooleanUtils.isTrue( paramMove );
        boolean skipError = BooleanUtils.isTrue( paramSkipError );
        for ( File current : listFile ) {
        	Mp3File mp3file = new Mp3File( current );
        	try {
        		ID3v1 id3v1 = mp3file.getId3v1Tag();
            	ID3v2 id3v2 = mp3file.getId3v2Tag();
            	String title = null;
            	String album = null;
            	String artist = null;
            	String track = null;
            	String disk = null;
            	if ( id3v2 != null ) {
            		title = id3v2.getTitle();
            		album  = id3v2.getAlbum();
            		artist = id3v2.getAlbumArtist();
            		if ( StringUtils.isEmpty( artist ) ) {
            			artist = id3v2.getArtist();
            		}
            		track = id3v2.getTrack();
            		disk = id3v2.getPartOfSet();
            	} else if ( id3v1 != null ) {
            		title = id3v1.getTitle();
            		album  = id3v1.getAlbum();
            		artist = id3v1.getArtist();
            		track = id3v1.getTrack();
            	}
            	if ( track.indexOf( "/" ) != -1 ) {
            		logger.debug( "disk {}", disk );
            		track = track.split("/")[0];
            		if ( StringUtils.isNotEmpty( disk ) ) {
            			if ( disk.indexOf( "/" ) != -1 ) {
            				disk = disk.split( "/" )[0];
            			}
            			if ( Integer.parseInt( disk ) != 0 ) {
            				track = disk+"_"+track;	
            			}
            		}
            	}
            	File outputDir = new File( paramOut );
            	if ( !doMove ) {
            		logger.info( "Artist -> {} , Album -> {}", artist, album );
            	}
            	String albumPath = artist.replaceAll( " ", "_" )+"__"+album.replaceAll( " ", "_" );
            	File albumDir = new File( outputDir, albumPath );
            	String fileName = track+"_-_"+title.replaceAll( " " , "_" )+".mp3";
            	fileName = fileName.replaceAll( "/" , "_" ); 
            	File outputFile = new File( albumDir, fileName );
            	if ( doMove ) {
            		if ( !albumDir.exists() ) {
                		albumDir.mkdirs();
                	}
            		boolean moveOk = current.renameTo( outputFile );
            		if ( !moveOk ) {
            			logger.info( "Failed to move {}", current.getCanonicalFile() ); 
            		}
            	}
            	logger.info( "{} -> {}", current.getCanonicalPath(), outputFile.getCanonicalPath() );	
        	} catch (Exception e) {
        		logger.info( "ERROR ON FILE : "+current.getCanonicalPath() );
        		if ( !skipError ) {
        			throw e;	
        		}
        	}
        }
		return res;
	}

}