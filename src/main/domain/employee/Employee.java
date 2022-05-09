package main.domain.employee;

import main.domain.accidnet.Accident;
import main.domain.contract.Contract;
import main.domain.insurance.Insuarance;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public class Employee {

	//private enum department;
	private int id;
	private String name;
	private String phone;
	//private enum position;
	public Contract m_Contract;
	public Insuarance m_Insuarance;
	public Accident m_Accident;

	public Employee(){

	}

	public void finalize() throws Throwable {

	}

	public void assessDamage(){

	}

	public void calculatePremium(){

	}

	public void concludeContract(){

	}

	public void develop(){

	}

	public void investigateDamage(){

	}

	public void planInsuarance(){

	}

	public void readAccident(){

	}

	public void readContract(){

	}

	public void registerAuthInfo(){

	}

	public void underwriting(){

	}

}