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

import java.io.IOException;

import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;

import com.arthurassuncao.cepview.IViewCEP;
import com.arthurassuncao.cepview.model.CEP;
import com.arthurassuncao.cepview.util.Constantes;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * @author Arthur Assuncao
 *
 */
public class TarefaObtemDadosCEP extends AsyncTask<String, CEP, Integer> {
	
	private IViewCEP activity;
	private ClienteHttp clienteHttp;
	
	public TarefaObtemDadosCEP(IViewCEP atividade) {
		this.activity = atividade;
	}
	
	@Override
	protected void onProgressUpdate(CEP... values) {
		super.onProgressUpdate(values);
		if(values != null){
			activity.setCamposEndereco(values[0]);
		}
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		activity.setCarregandoCEP(true);
	}

	@Override
	protected Integer doInBackground(String... arg0) {
		String cepBuscado = arg0[0];
		String urlCEP = Constantes.URL_API_CEP.replace("{CEP}", cepBuscado);
		Log.d("url", urlCEP);
		int codResposta = 0;
		clienteHttp = new ClienteHttp(urlCEP, "GET");
		
		clienteHttp.executar();
		codResposta = clienteHttp.getStatus();
		Gson gson = new Gson();
		String textoResposta;
		try {
			textoResposta = clienteHttp.getTextoResposta();
			if(textoResposta.equalsIgnoreCase("correiocontrolcep({\"erro\":true});")){ //jsonp retornado
				//cep invalido
			}
			else{
				try{
					CEP cep = gson.fromJson(textoResposta, CEP.class);
					publishProgress(cep);
					Log.d("teste", cep.getLogradouro());
				}
				catch(JsonParseException e){
					//cep invalido
				}
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (NullPointerException e){
			publishProgress((CEP[])null);
		}
		
		return codResposta;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		activity.setCarregandoCEP(false);
	}

}
