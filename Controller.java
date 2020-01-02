package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class Controller {
    @FXML
    private Button calculate;
    @FXML
    private TextField salary;
    @FXML
    private TextField groceries;
    @FXML
    private TextField gas;
    @FXML
    private TextField rent;
    @FXML
    private TextField insurance;
    @FXML
    private TextField loans;
    @FXML
    private TextField others;
    @FXML
    private TextField time;
    @FXML
    private TextField savingGoal;
    @FXML
    private RadioButton year;
    @FXML
    private RadioButton month;

    @FXML
    protected void handleButtonAction(ActionEvent event){
        Window owner = calculate.getScene().getWindow();
        double salaryInput = 0, groceriesInput = 0, gasInput = 0, rentInput = 0, insuranceInput = 0, loansInput=0,othersInput = 0,
                savingGoalInput = 0, unmodifiedTimeInput = 0, timeInput = 0;
        double freeMoney, monthlySpend, actualSave, realTime, extra, realYear, realMonth, realDay;
        String result_msg, realTime_msg;
        final int WEEK_OF_MONTH = 4, MONTH_OF_YEAR = 12;
        boolean inYear = year.isSelected();
        boolean inMonth = month.isSelected();

        //Get Data and Validate

        if(salary.getText().isEmpty()){
            MessageBox.showAlert(Alert.AlertType.ERROR, owner, "Missing Salary", "Please Enter Your Monthly Income!");
            return;
        } else {
            if(isNum(salary.getText())){
                salaryInput = Double.parseDouble(salary.getText());
            } else {
                formatError(owner);
                return;
            }
        }

        if(savingGoal.getText().isEmpty()){
            MessageBox.showAlert(Alert.AlertType.ERROR, owner, "Missing Goal", "Please Enter the Amount You Wish To Save Up!");
            return;
        } else {
            if(isNum(savingGoal.getText())){
                savingGoalInput = Double.parseDouble(savingGoal.getText());
                if (savingGoalInput <= 0){
                    MessageBox.showAlert(Alert.AlertType.ERROR, owner, "Data Error", "You don't want to save up at all?");
                    return;
                }
            } else {
                formatError(owner);
                return;
            }
        }

        if(time.getText().isEmpty()){
            MessageBox.showAlert(Alert.AlertType.ERROR, owner, "Missing Time Period", "How Long Are You Planning To Reach your Saving Goal?");
            return;
        } else {
            if(isNum(time.getText())){
                unmodifiedTimeInput = Double.parseDouble(time.getText());
                if (unmodifiedTimeInput <= 0){
                    MessageBox.showAlert(Alert.AlertType.ERROR, owner, "Data Error", "Please enter a REALISTIC Time!");
                    return;
                }
            } else {
                formatError(owner);
                return;
            }
        }

        if(!inYear && !inMonth) {
            MessageBox.showAlert(Alert.AlertType.ERROR, owner, "Missing Time", "Please Choose Either Month or Year");
            return;
        }

        //time validation
        if(inYear){
            timeInput = (int)unmodifiedTimeInput * MONTH_OF_YEAR;
        }else {
            timeInput = (int)unmodifiedTimeInput;
        }

        //WEEKLY DATA
        if(groceries.getText().isEmpty()){
            groceriesInput = 0;
        } else {
            if (isNum(groceries.getText())) {
                groceriesInput = Double.parseDouble(groceries.getText());
            } else {
                formatError(owner);
                return;
            }
        }

        if(gas.getText().isEmpty()){
            gasInput = 0;
        } else {
            if (isNum(gas.getText())) {
                gasInput = Double.parseDouble(gas.getText());
            } else {
                formatError(owner);
                return;
            }
        }

        //MONTHLY DATA
        if(rent.getText().isEmpty()){
            rentInput = 0;
        } else {
            if (isNum(rent.getText())) {
                rentInput = Double.parseDouble(rent.getText());
            } else {
                formatError(owner);
                return;
            }
        }

        if(insurance.getText().isEmpty()){
            insuranceInput = 0;
        } else {
            if (isNum(insurance.getText())) {
                insuranceInput = Double.parseDouble(insurance.getText());
            } else {
                formatError(owner);
                return;
            }
        }

        if(loans.getText().isEmpty()){
            loansInput = 0;
        } else {
            if (isNum(loans.getText())) {
                loansInput = Double.parseDouble(loans.getText());
            } else {
                formatError(owner);
                return;
            }
        }

        if(others.getText().isEmpty()){
            othersInput = 0;
        } else {
            if (isNum(others.getText())) {
                othersInput = Double.parseDouble(others.getText());
            } else {
                formatError(owner);
                return;
            }
        }

        //get total monthly spend
        monthlySpend = (groceriesInput + gasInput)* WEEK_OF_MONTH + rentInput + insuranceInput + loansInput + othersInput;
        if(monthlySpend > salaryInput){
            result_msg = "WARNING!!!\n You are spending MORE than what you earn!!!\n ";
            MessageBox.showAlert(Alert.AlertType.WARNING, owner, "CAUTION!", result_msg);
            return;
        }
        if(monthlySpend == salaryInput){
            result_msg = "You have ZERO free money to save up!!!";
            MessageBox.showAlert(Alert.AlertType.WARNING, owner, "CAUTION!", result_msg);
            return;
        }
        Math.round(timeInput);
        freeMoney = salaryInput - monthlySpend;
        actualSave = freeMoney * timeInput;
        //realistic time to reach the goal goal
        realTime = (savingGoalInput/(freeMoney * MONTH_OF_YEAR))*MONTH_OF_YEAR;
        if((int)realTime == (int) timeInput){
            realTime += 1;
        }
        if(realTime >= MONTH_OF_YEAR){
            Math.round(realTime);
            if(realTime % MONTH_OF_YEAR == 0){
                realYear = realTime/MONTH_OF_YEAR;
                realTime_msg = realYear  + (realYear > 1 ? " years" : " year");
            }else{
                realMonth = realTime % MONTH_OF_YEAR;
                Math.round(realMonth);
                realYear = (realTime - realMonth) / MONTH_OF_YEAR;
                realTime_msg = (int)realYear  + ((int)realYear > 1 ? " years and " : " year and ") +
                        (int)realMonth + ((int)realMonth >1? " months" : " month");
            }
        }else if (realTime < 1 && realTime > 0){
            realDay = realTime * 30;
            realTime_msg = (int)realDay +((int)realDay >1? " days" : " day") ;
        } else {
            realTime_msg = (int)realTime + ((int)realTime >1? " months" : " month");
        }

        if(actualSave < savingGoalInput || realTime > timeInput) {
            result_msg = "Unfortunately, with your current expenses, the maximum you can save up in " + (int)unmodifiedTimeInput+
                    (inYear ? " year" : " month") + ((int)unmodifiedTimeInput > 1 ? "s is " : " is ")  +
                     String.format("%,.2f", actualSave) + ". \n" +
                    "However, if you keep this up, in approximately " + realTime_msg +
                    " you can reach your goal !!! \n";
            MessageBox.showAlert(Alert.AlertType.INFORMATION, owner, "Result", result_msg);
            return;
        }

        if(actualSave >= savingGoalInput || realTime <= timeInput) {
            result_msg = "CONGRATULATIONS! YOU WILL REACH YOUR GOAL !!!\n";
            if(actualSave  > savingGoalInput && realTime < timeInput) {
                result_msg += "You will save up " + String.format("%,.2f", actualSave)+ " in " + (int)unmodifiedTimeInput +
                        (inYear ? " year" : " month") + ((int)unmodifiedTimeInput > 1 ? "s. \n" : "s. \n");
                extra = (actualSave - savingGoalInput)/timeInput;
                result_msg += "If you are in NO rush in reaching your goal, can spend extra up to " +
                        String.format("%,.2f", extra)+ " per month! \n";
            }
            MessageBox.showAlert(Alert.AlertType.INFORMATION, owner, "Result", result_msg);
            return;
        }



    }

    private static void formatError(Window owner){
        MessageBox.showAlert(Alert.AlertType.ERROR, owner, "Format Error", "All Field Must Be Digit");
    }

    private static boolean isNum(String text){
        try{
            double d = Double.parseDouble(text);
        }
        catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }

}
