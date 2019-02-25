package cn.cubercsl.slidysim.results;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import cn.cubercsl.slidysim.MyApplication;
import cn.cubercsl.slidysim.R;
import cn.cubercsl.slidysim.results.gen.ResultDao;

public class ResultFragment extends Fragment {

    private ListView listView;
    private List<Result> results;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result, container, false);
        listView = view.findViewById(R.id.resultsListView);
        Button resetbtn = view.findViewById(R.id.reset);
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResultDao resultDao = MyApplication.getInstance().getDaoSession().getResultDao();
                resultDao.deleteAll();
                listView.setAdapter(null);
            }
        });
        initResults();
        listView.setAdapter(new ResultAdapter(getContext(), R.layout.result_item, results));
        return view;
    }

    private void initResults() {
        ResultDao resultDao = MyApplication.getInstance().getDaoSession().getResultDao();
        QueryBuilder<Result> qb = resultDao.queryBuilder();
        results = qb.orderAsc(ResultDao.Properties.Time).limit(10).list();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            initResults();
            listView.setAdapter(new ResultAdapter(getContext(), R.layout.result_item, results));
        }
        super.onHiddenChanged(hidden);
    }
}
