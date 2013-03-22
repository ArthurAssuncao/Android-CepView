/*
	Copyright (c) 2013, Arthur Nascimento Assuncao
	All rights reserved.
	
	Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
	
		Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
		Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
	
	URL License: http://opensource.org/licenses/BSD-2-Clause
*/
package com.arthurassuncao.cepview.model;

import java.io.Serializable;

/**
 * @author Arthur Assuncao
 *
 */
public class CEP implements Serializable{
	private static final long serialVersionUID = -8249148862421407402L;
	private String bairro;
	private String logradouro; 
	private String cep; 
	private String uf;
	private String localidade;
	
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getLocalidade() {
		return localidade;
	}
	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}
	@Override
	public boolean equals(Object o) {
		CEP cepComparar = (CEP)o;
		if(this.getBairro().equalsIgnoreCase(cepComparar.getBairro()) && 
		   this.getCep().equalsIgnoreCase(cepComparar.getCep()) && 
		   this.getLocalidade().equalsIgnoreCase(cepComparar.getLocalidade()) && 
		   this.getLogradouro().equalsIgnoreCase(cepComparar.getLogradouro()) && 
		   this.getUf().equals(cepComparar.getUf())
		  ){
			return true;
		}
		return false;
	}
	
	
}
