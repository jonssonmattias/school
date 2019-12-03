package se.mau.mattiasjonsson.p1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.*;
import android.support.annotation.NonNull;
import android.util.*;
import android.widget.*;
import java.util.*;
import se.mau.mattiasjonsson.p1.database.*;
import android.arch.lifecycle.*;


public class MainViewModel extends AndroidViewModel {
    private String firstname, lastname;
    private Budget budget;
    private Repository repository;
    private MutableLiveData<List<Budget>> budgetList;

    public MainViewModel(@NonNull Application application){
        super(application);
        repository = new Repository(application);
        budget = new Budget();
    }

    public LiveData<List<Budget>> getBudgetList(int type, String username, String dateFrom, String dateTo) {
        budgetList = new MutableLiveData<>();
        loadList(type, username, dateFrom, dateTo);
        return budgetList;
    }

    private void loadList(int type, String username, String dateFrom, String dateTo) {
        if(dateFrom.isEmpty() && dateTo.isEmpty())
            budgetList.setValue(getAll(type,username));
        else if (dateFrom.isEmpty())
            budgetList.setValue(getAllTo(dateTo,type,username));
        else if(dateTo.isEmpty())
            budgetList.setValue(getAllFrom(dateFrom,type, username));
        else
            budgetList.setValue(getAllFromAndTo(dateFrom,dateTo,type,username));
    }

    public void setName(String firstname, String lastname, SharedPreferences.Editor editor ) {
        editor.putString("firstname", firstname);
        editor.putString("lastname", lastname);
        editor.apply();
    }
    public String getName(SharedPreferences sharedPreferences) {
        firstname = sharedPreferences.getString("firstname",null);
        lastname = sharedPreferences.getString("lastname",null);
        return firstname+" "+lastname;
    }

    public void add(String name, String amount, String date, String category, int type, String username){
        budget.setName(name);
        budget.setAmount(Integer.parseInt(amount));
        budget.setCategory(category);
        budget.setDate(date);
        budget.setType(type);
        budget.setUsername(username);
        repository.insert(budget);
    }

    public List<Budget> getAll(int type, String username){
        return repository.select(type,username);
    }
    public List<Budget> getAllFromAndTo(String from, String to, int type, String username){
        return repository.selectFromAndTo(from, to, type , username);
    }
    public List<Budget> getAllFrom(String from, int type, String username){
        return repository.selectFrom(from, type, username);
    }
    public List<Budget> getAllTo(String to, int type, String username){
        return repository.selectTo(to, type, username);
    }

    public int getType(boolean[] type) {
        for(int i=0;i<type.length;i++)
            if(type[i]) return i;
        return 2;
    }

    public int getTotal(List<Budget> budgetList){
        int sum=0;
        for(Budget budget : budgetList)
            sum+=budget.getAmount();
        return sum;
    }

    public String getType(int type) {
       return (type==0?"Income":"Expense");
    }

    public HashMap<String, Integer> getCategory(List<Budget> budgetList){
        HashMap<String, Integer> hashMap = new HashMap<>();
        int salary = 0, otherIncome=0, food=0, leisure=0, travel=0, accommodation=0, otherExpense=0;
        for(Budget budget : budgetList){
            switch (budget.getCategory().toLowerCase()){
                case "salary": salary++; break;
                case "food": food++; break;
                case "leisure": leisure++; break;
                case "travel": travel++; break;
                case "accommodation": accommodation++; break;
                default:
                    if(budget.getType()==0)otherIncome++;
                    else otherExpense++;
                    break;
            }
        }
        hashMap.put("Salary", salary);
        hashMap.put("Other income", otherIncome);
        hashMap.put("Food",food);
        hashMap.put("Leisure", leisure);
        hashMap.put("Travel", travel);
        hashMap.put("Accommodation", accommodation);
        hashMap.put("Other expense", otherExpense);

        return hashMap;
    }

    public Object getCategory(int type, String category, Context context){
        if(type==1){
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(getImage(category));
            return imageView;
        }
        else {
            TextView t4v = new TextView(context);
            t4v.setText(category);
            return t4v;
        }
    }

    public int getImage(String category){
        switch (category){
            case "Food": return R.drawable.food;
            case "Leisure": return R.drawable.leisure;
            case "Travel": return R.drawable.travel;
            case "Accommodation": return R.drawable.accommondation;
            default: return R.drawable.other;
        }
    }

    public String[] parseQRCode(String qrCodeResult){
        String[] split =qrCodeResult.split(","), result = new String[5];
        for(int i=0;i<split.length;i++){
            result[i] = split[i].split(":")[1];
            Log.d("test", result[i]);
        }
        return result;
    }

    public void getInitialName(EditText etFirstname, EditText etLastname, SharedPreferences sharedPreferences) {
        getName(sharedPreferences);
        if(firstname!=null && lastname!=null){
            etFirstname.setText(firstname);
            etLastname.setText(lastname);
        }
    }
}
