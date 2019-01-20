package br.com.fiap.twitter;

import java.util.Comparator;
import java.util.Date;

public class NomeData {

	private String nome;
	private String primeiroNome;
	private String ultimoNome;
	private Date date;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
		if (nome.indexOf(' ') >= 0) {
			primeiroNome = nome.substring(0, nome.indexOf(' '));
			ultimoNome = nome.substring(nome.indexOf(' ') + 1);
		} else {
			this.primeiroNome = nome;
		}
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPrimeiroNome() {
		return primeiroNome;
	}

	public void setPrimeiroNome(String primeiroNome) {
		this.primeiroNome = primeiroNome;
	}

	public String getUltimoNome() {
		return ultimoNome;
	}

	public void setUltimoNome(String ultimoNome) {
		this.ultimoNome = ultimoNome;
	}

	public static Comparator<NomeData> name = new Comparator<NomeData>() {

		@Override
		public int compare(NomeData a0, NomeData a1) {
			return (a0.getNome().compareTo(a1.getNome()));
		}

	};

	public static Comparator<NomeData> data = new Comparator<NomeData>() {

		@Override
		public int compare(NomeData a0, NomeData a1) {

			return (a0.getDate().compareTo(a1.getDate()));
		}
	};

	@Override
	public String toString() {
		return "NomeData [nome=" + nome + ", primeiroNome=" + primeiroNome
				+ ", ultimoNome=" + ultimoNome + ", date=" + date + "]";
	}

}
