package utility;

import dnl.utils.text.table.TextTable;
import insuranceCompany.application.dao.employee.EmployeeDaoImpl;
import insuranceCompany.application.domain.employee.Employee;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class textutiltest {

    @Test
    void text() throws IllegalAccessException {
        EmployeeDaoImpl dao = new EmployeeDaoImpl();
        ArrayList<Employee> employees = (ArrayList<Employee>) dao.readAllCompEmployee();

        Class clazz = Employee.class;
        Field[] field = clazz.getDeclaredFields();

        String[] f = new String[field.length];

        for (int i = 0; i < field.length; i++) {
            f[i] = field[i].getName();
        }


        Object[][] fields = new Object[employees.size()][field.length];
        for (int i = 0; i < employees.size(); i++) {
            Field[] declaredFields = employees.get(i).getClass().getDeclaredFields();
            for (int j = 0; j< field.length; j++) {
                declaredFields[j].setAccessible(true);
                fields[i][j] = declaredFields[j].get(employees.get(i));
            }
        }




        TextTable tx = new TextTable(f,fields);
        tx.printTable();
    }

    @Test
    void test2() throws IllegalAccessException {
        EmployeeDaoImpl dao = new EmployeeDaoImpl();
        ArrayList<Employee> employees = (ArrayList<Employee>) dao.readAllCompEmployee();

        printTable(Employee.class,employees);
    }

    public static void printTable(Class<?> clz, ArrayList<?> clzzs) throws IllegalAccessException {
        Class clazz = clz.getClass();
        Field[] field = clazz.getDeclaredFields();

        String[] f = new String[field.length];

        for (int i = 0; i < field.length; i++) {
            f[i] = field[i].getName();
        }


        Object[][] fields = new Object[clzzs.size()][field.length];
        for (int i = 0; i < clzzs.size(); i++) {
            Field[] declaredFields = clzzs.get(i).getClass().getDeclaredFields();
            for (int j = 0; j< field.length; j++) {
                declaredFields[j].setAccessible(true);
                fields[i][j] = declaredFields[j].get(clzzs.get(i));
            }
        }




        TextTable tx = new TextTable(f,fields);
        tx.printTable();
    }
}
