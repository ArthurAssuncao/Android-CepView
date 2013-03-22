/*
	Copyright (c) 2013, Arthur Nascimento Assuncao
	All rights reserved.
	
	Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
	
		Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
		Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
	
	URL License: http://opensource.org/licenses/BSD-2-Clause
*/
package com.arthurassuncao.cepview.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.Gson;

public class ClienteHttp {
	private HttpClient cliente;
	private HttpRequestBase httpRequest;
	private String endereco;
	private String tipoRequisicao;
	private List<NameValuePair> parametros;
	private HttpResponse resposta;
	private final String POST = "POST" ;
	private final String GET = "GET" ;

	public ClienteHttp(String endereco, String tipoRequisicao) {
		this.endereco = endereco;
		this.cliente = new DefaultHttpClient();
		this.tipoRequisicao = tipoRequisicao;
		this.parametros = new ArrayList<NameValuePair>();

		if (this.tipoRequisicao.equalsIgnoreCase(POST)){
			httpRequest = new HttpPost(this.endereco);
		}
		else if(this.tipoRequisicao.equalsIgnoreCase(GET)){
			httpRequest = new HttpGet(this.endereco);
		}
		else{
			new IllegalArgumentException("Tipo de requisição inválido");
		}
	}

	public void addParametro(String codigo, String valor) {
		BasicNameValuePair valores = new BasicNameValuePair(codigo, valor);
		parametros.add(valores);
	}

	public void limparParametros() {
		parametros.removeAll(parametros);
	}

	public void executar() {
		try {
			if (this.httpRequest instanceof HttpGet) {
				resposta = cliente.execute(this.httpRequest);
			}
			else if(this.httpRequest instanceof HttpPost) {
				UrlEncodedFormEntity urlParametros = new UrlEncodedFormEntity(parametros, HTTP.UTF_8);
				((HttpPost)this.httpRequest).setEntity(urlParametros);
				resposta = cliente.execute(this.httpRequest);
			}
			else{
				new IllegalArgumentException("Tipo de requisição inválido");
			}
		}
		catch (Exception e) {
			Log.d("erro", e.toString());
			resposta = null;
		}
	}

	public int getStatus() {
		if (resposta != null) {
			return resposta.getStatusLine().getStatusCode();
		}
		return 408;
	}

	public String getTextoResposta() throws ParseException, IOException{
		HttpEntity entity = resposta.getEntity();
		String textoResposta = EntityUtils.toString(entity);
		return textoResposta;
	}

	public InputStream getContent() throws IllegalStateException, IOException {
		return resposta.getEntity().getContent();
	}

	public Object obterJson(Class<?> classe) {
		Object json = null;
		try {
			HttpEntity httpEntity = resposta.getEntity();

			if (httpEntity != null) {
				Gson gson = new Gson();

				BufferedReader in = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
				json = gson.fromJson(in, classe);
				in.close();
				return json;
			}
		}
		catch (ClientProtocolException e) {
			Log.d("ClientProtocolException: ", e.toString());
		}
		catch (IOException e) {
			Log.d("IOException: ", e.toString());
		}
		return json;
	}
}