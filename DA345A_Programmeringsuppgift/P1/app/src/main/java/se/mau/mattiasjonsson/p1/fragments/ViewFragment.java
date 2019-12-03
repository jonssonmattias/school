package se.mau.mattiasjonsson.p1.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.arch.lifecycle.*;
import lecho.lib.hellocharts.model.*;
import lecho.lib.hellocharts.view.PieChartView;
import se.mau.mattiasjonsson.p1.database.*;
import se.mau.mattiasjonsson.p1.MainViewModel;
import se.mau.mattiasjonsson.p1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFragment extends Fragment {

    private MainViewModel mainViewModel;
    private TableLayout tlView;
    private List<Budget> budgetList;
    private EditText etDateFrom, etDateTo;
    private Button btnViewBudget;
    private SharedPreferences sharedPreferences;
    private RadioButton rbAll, rbIncome, rbExpenses, rbTable, rbChart;
    private TextView tvTotal;
    private LinearLayout linearLayout;
    private ScrollView scrollView;
    private PieChartView pieChartView;
    private final Observer<List<Budget>> budgetObserver = new Observer<List<Budget>>() {
        @Override
        public void onChanged(@Nullable final List<Budget> budgetList) {
            toggleResult();
            if(rbTable.isChecked())
                setTable(budgetList);
            else
                setPieChart(budgetList);
        }
    };

    public ViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        sharedPreferences = getActivity().getSharedPreferences("P1", Activity.MODE_PRIVATE);
        initializeComponents(view);
        btnViewBudget.setOnClickListener(new ButtonListener());
        if(savedInstanceState!=null){
            mainViewModel.getBudgetList(2, mainViewModel.getName(sharedPreferences), "", "").observe(this, budgetObserver);
        }
        mainViewModel.getBudgetList(2, mainViewModel.getName(sharedPreferences), "", "").observe(this, budgetObserver);
        return view;
    }

    private void initializeComponents(View view){
        etDateFrom = view.findViewById(R.id.etDateFrom);
        etDateTo = view.findViewById(R.id.etDateTo);
        btnViewBudget = view.findViewById(R.id.btnViewBudget);
        rbAll = view.findViewById(R.id.rbAll);
        rbExpenses = view.findViewById(R.id.rbExpenses);
        rbIncome = view.findViewById(R.id.rbIncome);
        rbTable = view.findViewById(R.id.rbTable);
        rbChart = view.findViewById(R.id.rbChart);
        tvTotal = view.findViewById(R.id.tvTotal);
        linearLayout = view.findViewById(R.id.linearlayout);
    }

    private void setPieChart(List<Budget> budgetList){
        HashMap<String, Integer> hashMap = mainViewModel.getCategory(budgetList);
        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(hashMap.get("Salary"), Color.BLUE).setLabel("Salary"));
        pieData.add(new SliceValue(hashMap.get("Other income"), Color.GRAY).setLabel("Other income"));
        pieData.add(new SliceValue(hashMap.get("Food"), Color.RED).setLabel("Food"));
        pieData.add(new SliceValue(hashMap.get("Leisure"), Color.MAGENTA).setLabel("Leisure"));
        pieData.add(new SliceValue(hashMap.get("Travel"), Color.YELLOW).setLabel("Travel"));
        pieData.add(new SliceValue(hashMap.get("Accommodation"), Color.CYAN).setLabel("Accommodation"));
        pieData.add(new SliceValue(hashMap.get("Other expense"), Color.GREEN).setLabel("Other expense"));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(10);
        pieChartData.setHasCenterCircle(true).setCenterText1("Budget").setCenterText1FontSize(20);
        pieChartView.setPieChartData(pieChartData);

    }

    private void setTable(List<Budget> budgetList) {
        tvTotal.setText("Balance:"+mainViewModel.getTotal(budgetList));
        TableRow tbrow0 = new TableRow(getContext());
        TextView tv0 = new TextView(getContext());
        tv0.setText(" Title ");
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(getContext());
        tv1.setText(" Amount ");
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(getContext());
        tv2.setText(" Date ");
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(getContext());
        tv3.setText(" Category ");
        tbrow0.addView(tv3);
        TextView tv4 = new TextView(getContext());
        tv4.setText(" Type ");
        tbrow0.addView(tv4);
        tlView.addView(tbrow0);
        for (int i = 0; i < budgetList.size(); i++) {
            TableRow tbrow = new TableRow(getContext());
            TextView t1v = new TextView(getContext());
            t1v.setText(budgetList.get(i).getName());
            tbrow.addView(t1v);
            TextView t2v = new TextView(getContext());
            t2v.setText(Integer.toString(budgetList.get(i).getAmount()));
            tbrow.addView(t2v);
            TextView t3v = new TextView(getContext());
            t3v.setText(budgetList.get(i).getDate());
            tbrow.addView(t3v);
            tbrow.addView((View) mainViewModel.getCategory(budgetList.get(i).getType(), budgetList.get(i).getCategory(), getContext()));
            TextView t5v = new TextView(getContext());
            t5v.setText(mainViewModel.getType(budgetList.get(i).getType()));
            tbrow.addView(t5v);
            tbrow.setOnClickListener(new RowSetListener(budgetList.get(i)));
            tlView.addView(tbrow);

        }
    }

    private void toggleResult(){
        if(rbTable.isChecked()){
            if(tlView!=null){
                scrollView.removeAllViews();
                tlView.removeAllViews();
            }
            tlView = new TableLayout(getContext());
            scrollView = new ScrollView(getContext());
            linearLayout.removeView(pieChartView);
            linearLayout.removeView(tlView);
            scrollView.addView(tlView);
            linearLayout.addView(scrollView);
        }
        else {
            linearLayout.removeView(scrollView);
            linearLayout.removeView(pieChartView);
            pieChartView = new PieChartView(getContext());
            linearLayout.addView(pieChartView);
        }
    }

    private class RowSetListener implements View.OnClickListener {
        Budget budget;
        public RowSetListener(Budget budget){
            this.budget=budget;
        }
        public void onClick(View v) {
            String message = getResources().getString( R.string.name)+budget.getName()+
                    "\n"+getResources().getString(R.string.amount)+budget.getAmount()+
                    "\n"+getResources().getString(R.string.category)+budget.getCategory()+
                    "\n"+getResources().getString(R.string.date)+budget.getDate();
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            String dateFrom=etDateFrom.getText().toString(), dateTo=etDateTo.getText().toString();
            boolean[] type = {rbIncome.isChecked(),rbExpenses.isChecked(), rbAll.isChecked()};
            mainViewModel.getBudgetList( mainViewModel.getType(type), mainViewModel.getName(sharedPreferences), dateFrom, dateTo).observe(ViewFragment.this, budgetObserver);
        }
    }


}
