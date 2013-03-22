package com.arthurassuncao.cepview;

import com.arthurassuncao.cepview.model.CEP;

public interface ICep {
	public String getCEP();
	public void setCEP(String cep);
	public String getEndereco();
	public void setEndereco(String endereco);
	public String getNumeroEndereco();
	public void setNumeroEndereco(String numeroEndereco);
	public String getBairro();
	public void setBairro(String bairro);
	public String getCidade();
	public void setCidade(String cidade);
	public String getEstado();
	public void setEstado(String estado);
	public CEP getCepAtual();
}
