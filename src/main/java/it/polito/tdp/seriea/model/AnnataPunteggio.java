package it.polito.tdp.seriea.model;

public class AnnataPunteggio {

	private Integer anno;
	private Integer punti;
	private String stag;
	
	public AnnataPunteggio(Integer anno, Integer punti, String stag) {
		super();
		this.anno = anno;
		this.punti = punti;
		this.setStag(stag);
	}
	
	
	public Integer getAnno() {
		return anno;
	}
	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	public Integer getPunti() {
		return punti;
	}
	public void setPunti(Integer punti) {
		this.punti = punti;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anno == null) ? 0 : anno.hashCode());
		result = prime * result + ((punti == null) ? 0 : punti.hashCode());
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
		AnnataPunteggio other = (AnnataPunteggio) obj;
		if (anno == null) {
			if (other.anno != null)
				return false;
		} else if (!anno.equals(other.anno))
			return false;
		if (punti == null) {
			if (other.punti != null)
				return false;
		} else if (!punti.equals(other.punti))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return anno + " punti: " + punti;
	}


	public String getStag() {
		return stag;
	}


	public void setStag(String stag) {
		this.stag = stag;
	}


}
