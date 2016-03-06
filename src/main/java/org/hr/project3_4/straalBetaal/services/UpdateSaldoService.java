package org.hr.project3_4.straalBetaal.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hr.project3_4.straalBetaal.dao.UpdateSaldoDAO;
import org.hr.project3_4.straalBetaal.entities.User;

public class UpdateSaldoService {

	private Map<Long, User> saldos = new HashMap<>();


	public UpdateSaldoService() {
		//saldos.put(1L, new User("jasonP", "1000"));
	}


	public User updateSaldo() {
		UpdateSaldoDAO update = new UpdateSaldoDAO();


		try {
			saldos.put(1L, update.checkBalance());
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("FAILURE!");
			e.printStackTrace();
		}

		return saldos.get(1L);
	}

}
