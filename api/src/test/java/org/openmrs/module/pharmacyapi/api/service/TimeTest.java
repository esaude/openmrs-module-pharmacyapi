package org.openmrs.module.pharmacyapi.api.service;

import java.util.Calendar;

public class TimeTest {

	public static void main(String[] args) {

		calculaChort24Meses();
		System.out.println("-----------------");
		calculaChort36Meses();

	}

	static void calculaChort36Meses() {

		Calendar dataInicial = Calendar.getInstance();

		dataInicial.set(Calendar.YEAR, 2017);
		dataInicial.set(Calendar.MONTH, 4);
		dataInicial.set(Calendar.DAY_OF_MONTH, 31);
		Calendar dataFinal = (Calendar) dataInicial.clone();

		dataFinal.add(Calendar.MONTH, -36);
		dataInicial.add(Calendar.MONTH, -24);
		dataInicial.add(Calendar.DAY_OF_MONTH, 1);

		dataInicial.add(Calendar.MONTH, -15);

		System.out.println("Data Inicial 36 Meses --> " + dataInicial.getTime());
		System.out.println("Data Final 36 Meses --> " + dataFinal.getTime());

	}

	static void calculaChort24Meses() {

		Calendar dataInicial = Calendar.getInstance();

		dataInicial.set(Calendar.YEAR, 2017);
		dataInicial.set(Calendar.MONTH, 4);
		dataInicial.set(Calendar.DAY_OF_MONTH, 31);
		Calendar dataFinal = (Calendar) dataInicial.clone();

		dataFinal.add(Calendar.MONTH, -24);
		dataInicial.add(Calendar.MONTH, -24);
		dataInicial.add(Calendar.DAY_OF_MONTH, 1);

		dataInicial.add(Calendar.MONTH, -3);

		System.out.println("Data Inicial 24 Meses --> " + dataInicial.getTime());
		System.out.println("Data Final 24 Meses --> " + dataFinal.getTime());
	}
}
