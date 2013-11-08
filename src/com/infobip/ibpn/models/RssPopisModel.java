package com.infobip.ibpn.models;

public class RssPopisModel {

	private int id;
	private String rssFeed;
	private String opis;
	private int idRssSource;

	public RssPopisModel() {

	}

	public RssPopisModel(String rssFeed, String opis, int idRssSource) {
		this.rssFeed = rssFeed;
		this.opis = opis;
		this.idRssSource = idRssSource;
	}

	public RssPopisModel(int id, String rssFeed, String opis, int idRssSource) {
		this.id = id;
		this.rssFeed = rssFeed;
		this.opis = opis;
		this.idRssSource = idRssSource;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRssFeed() {
		return rssFeed;
	}

	public void setRssFeed(String rssFeed) {
		this.rssFeed = rssFeed;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public int getIdRssSource() {
		return idRssSource;
	}

	public void setIdRssSource(int idRssSource) {
		this.idRssSource = idRssSource;
	}

	@Override
	public String toString() {
		return "RssPopisModel [id=" + id + ", rssFeed=" + rssFeed + ", opis="
				+ opis + ", idRssSource=" + idRssSource + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + idRssSource;
		result = prime * result + ((opis == null) ? 0 : opis.hashCode());
		result = prime * result + ((rssFeed == null) ? 0 : rssFeed.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RssPopisModel other = (RssPopisModel) obj;
		if (id != other.id)
			return false;
		if (idRssSource != other.idRssSource)
			return false;
		if (rssFeed == null) {
			if (other.rssFeed != null)
				return false;
		} else if (!rssFeed.equals(other.rssFeed))
			return false;
		return true;
	}

}
