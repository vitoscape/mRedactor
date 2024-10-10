package model;

public class Album {
	
	private String artist;
	private String album;
	private String genre;
	private String year;
	private String trackTotal;
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public void setTrackTotal(String trackTotal) {
		this.trackTotal = trackTotal;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public String getYear() {
		return year;
	}
	
	public String getTrackTotal() {
		return trackTotal;
	}
}
