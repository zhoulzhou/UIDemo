package com.example.uidemo.logic.player;

import java.io.Serializable;

public class Song implements Serializable{
//	private static final long serialVersionUID = -5827842851684666454L; //注意此处的值。
	public static final int TYPE_ONLINE = 1;
	public static final int TYPE_LOCAL = 2;
	public static final int TYPE_EXTERNAL = 3;
	public int type = 0;
	
	/**
	 * 百度音乐song id
	 */
	public long songId = -1;
	
	/**
	 * 歌曲版本
	 */
	public String songVersion;
	
	/**
	 * 本地音乐的数据库id
	 */
	public long dbId = -1;
	
	/**
	 * 歌曲路径
	 */
	public String path;
	
	/**
	 * 歌曲名
	 */
	public String songName;
	
	/**
	 * 专辑ID
	 */
	public long albumId;
	
	/**
	 * 专辑名
	 */
	public String albumName;
	
	/**
	 * 歌手ID
	 */
	public long artistId;
	
	/**
	 * 歌手名
	 */
	public String artistName;
	
	/**
	 * 来源，用于数据统计
	 */
	public String from;
	
	/**
	 * 歌曲长度
	 */
	public long duration;
	
	/**
	 * bitrate
	 */
	public int bitrate;
	
	/**
	 * 曲风
	 */
	public int equalizerType;
	
	/**
	 * 回放增益
	 */
	public double replayGainLevel;
	
	/**
	 * 歌曲文件链接
	 */
	public String fileLink;
	
	/**
	 * 歌词链接
	 */
	public String lyricLink;
	
	/**
	 * 专辑图片链接
	 */
	public String albumImageLink;
	
	/**
	 * 专辑图片文件路径
	 */
	public String albumImagePath;
	
	/**
	 * 歌手图片文件路径
	 */
	public String artistImagePath;
	
	/**
	 * 歌词文件路径
	 */
	public String lyricPath;
	
	/**
	 * 所有编码
	 */
	public String allRates;
	
	/**
	 * 是否有高品质
	 */
	public int haveHigh;
	
	/**
	 * 付费标志
	 */
	public int charge;
	
	/**
	 * 第三方链接
	 */
	public String showUrl;
	
	/**
	 * 资源类型
	 */
	public String resourceType;
	
	public static final String WHITE_RESOURCE_TYPE = "0";
	public static final String THIRD_RESOURCE_TYPE = "2";
	public static final String GRAY_RESOURCE_TYPE = "3";
	
	@Override
	public String toString(){
		return "song: " + songName + ", type: " + type + ", songid: " + songId + ", dbid: " + dbId + ", path: " + path + ",from:"+from; 
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		
		if(obj instanceof Song){
			Song s = (Song)obj;
			if(type == s.type && type == Song.TYPE_LOCAL && dbId == s.dbId){
				return true;
			} else if(type == s.type && type == Song.TYPE_ONLINE && songId == s.songId){
				return true;
			}
		}
		return false;
	}
}