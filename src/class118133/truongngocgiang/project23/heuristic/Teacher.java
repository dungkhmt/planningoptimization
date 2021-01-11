package heuristic;

public class Teacher {
    private int[] _scheduler; 
    private int _half_day_period;
    private int _n_day;
    // private int _

    public Teacher(int half_day_period, int n_day) {
        this._half_day_period = half_day_period;
        this._n_day = n_day;
        this._scheduler = new int[half_day_period * 2 * n_day + 1];
    }

    public int add(int cls, int sub, int period) {
        for (int it = 0; it < 2 * _n_day; ++it) {
            int tmp = _half_day_period * it;
            int en = (it + 1) * _half_day_period;

            for (int st = 0; st < _half_day_period - period+1; ++st) 
            if (check_start(tmp + st, period) == 1 && st + period - 1 < en) {
                for (int i = 0; i < period; ++i) _scheduler[tmp + st + i] = 1;
                return tmp + st+1;
            }
        }
        return 0;
    }

    private int check_start(int st, int period) {
        for (int i = 0; i < period; ++i)
        if (_scheduler[st+i] != 0) return 0;
        return 1;
    }
}
