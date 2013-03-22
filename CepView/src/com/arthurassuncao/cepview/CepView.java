/*
	Copyright (c) 2013, Arthur Nascimento Assuncao
	All rights reserved.
	
	Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
	
		Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
		Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
	
	URL License: http://opensource.org/licenses/BSD-2-Clause
*/
package com.arthurassuncao.cepview;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.arthurassuncao.cepview.model.CEP;
import com.arthurassuncao.cepview.net.TarefaObtemDadosCEP;

/**
 * @author Arthur Assuncao
 *
 */
public class CepView extends RelativeLayout implements IViewCEP, ICep{

	private static final String msgErroCampoVazio = "Campo %s deve ser preenchido";

	private Context context;
	private EditText campoCEP;
	private EditText campoEndereco;
	private EditText campoNumeroEndereco;
	private EditText campoBairro;
	private EditText campoCidade;
	private Spinner spinnerEstado;

	private CEP cepAtual;

	private TarefaObtemDadosCEP tarefaCEP;
	private ProgressBar barraProgressoCep;
	private Map<String, Integer> mapEstados;

	public CepView(Context context) {
		this(context, null);
	}

	public CepView(Context context, AttributeSet attr) {
		super(context,attr);
		this.context = context;
		
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater layoutInflater;
		layoutInflater = (LayoutInflater)getContext().getSystemService(infService);
		layoutInflater.inflate(R.layout.layout_cep_view, this, true);

		this.campoCEP = (EditText)findViewById(R.id.cep_view_campo_texto_cep);
		this.campoEndereco = (EditText)findViewById(R.id.cep_view_campo_texto_endereco);
		this.campoNumeroEndereco = (EditText)findViewById(R.id.cep_view_campo_texto_numero_endereco);
		this.campoBairro = (EditText)findViewById(R.id.cep_view_campo_texto_bairro);
		this.campoCidade = (EditText)findViewById(R.id.cep_view_campo_texto_cidade);
		this.spinnerEstado = (Spinner)findViewById(R.id.cep_view_spinner_estado);
		this.barraProgressoCep = (ProgressBar)findViewById(R.id.cep_view_progress_bar_cep);
		
		ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(this.context, R.array.cep_view_vetor_estados, android.R.layout.simple_spinner_item);
		adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerEstado.setAdapter(adapterEstado);
		mapEstados = new HashMap<String, Integer>();
		String[] estados = getResources().getStringArray(R.array.cep_view_vetor_estados);
		for (int i = 0; i < estados.length; i++){
			mapEstados.put(estados[i], i);
		}

		this.changeFieldsAttributes();

		this.addFieldsListeners();
	}

	private void changeFieldsAttributes(){
		this.barraProgressoCep.setVisibility(View.INVISIBLE);
	}

	private void addFieldsListeners(){
		this.campoCEP.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					buscaCep();
				}
			}
		});
	}

	private boolean mudouCep(){
		if(cepAtual != null){
			CEP novoCep = new CEP();
			novoCep.setCep(campoCEP.getText().toString());
			novoCep.setBairro(campoBairro.getText().toString());
			novoCep.setLocalidade(campoCidade.getText().toString());
			novoCep.setLogradouro(campoEndereco.getText().toString());
			novoCep.setUf((String)spinnerEstado.getSelectedItem());
			return !cepAtual.equals(novoCep);
		}
		return true;
	}

	private void buscaCep(){
		if(mudouCep()){
			tarefaCEP = new TarefaObtemDadosCEP(this);
			String cep = campoCEP.getText().toString();
			if(!cep.equals("") && cep.length() == 8){
				tarefaCEP.execute(cep);
			}
		}
	}

	@Override
	public void setCarregandoCEP(boolean carregando) {
		if(carregando){
			barraProgressoCep.setVisibility(View.VISIBLE);
		}
		else{
			barraProgressoCep.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void setCamposEndereco(CEP cep) {
		Locale local = new Locale("pt", "br");
		cepAtual = cep;
		campoCEP.setText(cep.getCep().toUpperCase(local));
		campoBairro.setText(cep.getBairro().toUpperCase(local));
		campoCidade.setText(cep.getLocalidade().toUpperCase(local));
		campoEndereco.setText(cep.getLogradouro().toUpperCase(local));
		verificaCampoBairro();
		verificaCampoCidade();
		verificaCampoEndereco();
		String estado = cep.getUf().toUpperCase(local);
		if(mapEstados.containsKey(estado)){
			spinnerEstado.setSelection(mapEstados.get(estado));
		}
	}

	private boolean verificaCampoEndereco(){
		String endereco = campoEndereco.getText().toString().trim();
		boolean validos = true;
		if(endereco.equals("")){
			campoEndereco.setError(String.format(msgErroCampoVazio, getResources().getString(R.string.cep_view_endereco)));
			validos = false;
		}
		else{
			campoEndereco.setError(null);
		}
		return validos;
	}

	private boolean verificaCampoBairro(){
		String bairro = campoBairro.getText().toString().trim();
		boolean validos = true;
		if(bairro.equals("")){
			campoBairro.setError(String.format(msgErroCampoVazio, getResources().getString(R.string.cep_view_bairro)));
			validos = false;
		}
		else{
			campoBairro.setError(null);
		}
		return validos;
	}

	private boolean verificaCampoCidade(){
		String cidade = campoCidade.getText().toString().trim();
		boolean validos = true;
		if(cidade.equals("")){
			campoCidade.setError(String.format(msgErroCampoVazio, getResources().getString(R.string.cep_view_cidade)));
			validos = false;
		}
		else{
			campoCidade.setError(null);
		}
		return validos;
	}
	
	public EditText getCampoCEP() {
		return campoCEP;
	}

	public void setCampoCEP(EditText campoCEP) {
		this.campoCEP = campoCEP;
	}

	public EditText getCampoEndereco() {
		return campoEndereco;
	}

	public void setCampoEndereco(EditText campoEndereco) {
		this.campoEndereco = campoEndereco;
	}

	public EditText getCampoNumeroEndereco() {
		return campoNumeroEndereco;
	}

	public void setCampoNumeroEndereco(EditText campoNumeroEndereco) {
		this.campoNumeroEndereco = campoNumeroEndereco;
	}

	public EditText getCampoBairro() {
		return campoBairro;
	}

	public void setCampoBairro(EditText campoBairro) {
		this.campoBairro = campoBairro;
	}

	public EditText getCampoCidade() {
		return campoCidade;
	}

	public void setCampoCidade(EditText campoCidade) {
		this.campoCidade = campoCidade;
	}

	public Spinner getSpinnerEstado() {
		return spinnerEstado;
	}

	public void setSpinnerEstado(Spinner spinnerEstado) {
		this.spinnerEstado = spinnerEstado;
	}

	public CEP getCepAtual() {
		return cepAtual;
	}

	public void setCepAtual(CEP cepAtual) {
		this.cepAtual = cepAtual;
	}

	public ProgressBar getBarraProgressoCep() {
		return this.barraProgressoCep;
	}

	@Override
	public String getCEP() {
		return this.campoCEP.getText().toString();
	}

	@Override
	public void setCEP(String cep) {
		this.campoCEP.setText(cep);
	}

	@Override
	public String getEndereco() {
		return this.campoEndereco.getText().toString();
	}

	@Override
	public void setEndereco(String endereco) {
		this.campoEndereco.setText(endereco);
	}

	@Override
	public String getNumeroEndereco() {
		return this.campoNumeroEndereco.getText().toString();
	}

	@Override
	public void setNumeroEndereco(String numeroEndereco) {
		this.campoNumeroEndereco.setText(numeroEndereco);
	}

	@Override
	public String getBairro() {
		return this.campoBairro.getText().toString();
	}

	@Override
	public void setBairro(String bairro) {
		this.campoBairro.setText(bairro);
	}

	@Override
	public String getCidade() {
		return this.campoCidade.getText().toString();
	}

	@Override
	public void setCidade(String cidade) {
		this.campoCidade.setText(cidade);
	}

	@Override
	public void setEstado(String estado) {
		if(mapEstados.containsKey(estado)){
			this.spinnerEstado.setSelection(mapEstados.get(estado));
		}
		else{
			throw new IllegalArgumentException("Estado não existe");
		}
	}

	@Override
	public String getEstado() {
		return (String)this.spinnerEstado.getSelectedItem();
	}
}
