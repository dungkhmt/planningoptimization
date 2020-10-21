import numpy as np
from itertools import combinations

class NQueen:
    def __init__(self, n, num_iters=1000):
        if isinstance(n, int):
            self.state = np.random.permutation(n)
            self.n = n
        elif isinstance(n, np.ndarray):
            self.state = n
            self.n = len(n)
        self._counts = self._count_per_col()
        self._total_count = self._counts.sum()
        self.num_iters = num_iters
        
    def _count_total(self):
        count = 0
        for col1, row1 in enumerate(self.state):
            for col2, row2 in enumerate(self.state[col1+1:], col1+1):
                if row1 == row2 or abs(row1-row2) == abs(col1-col2):
                    count += 1
        return count
    
    def _count_per_col(self):
        all_count = np.zeros_like(self.state)
        for col, row in enumerate(self.state):
            count = 0
            for c, r in enumerate(self.state):
                if c == col: continue
                if row == r or abs(r-row) == abs(c-col):
                    count += 1
            all_count[col] = count
        return all_count
    
    def _count_per_queen(self, c, r):
        count = 0
        for col, row in enumerate(self.state):
            if c == col: continue
            if row == r or abs(r-row) == abs(c-col):
                count += 1
        return count
    
    def _find_candidate_rows(self, col):
        best_candidates = [self.state[col]]
        current_violations = self._counts[col]
        # current_violations = self._counts[col]
        for r in range(self.n):
            if r == self.state[col]: continue
            violations = self._count_per_queen(col, r)
            
            if violations < current_violations:
                best_candidates = [r]
                current_violations = violations
            
            if violations == current_violations:
                best_candidates.append(r)
        return best_candidates
    
    def solve(self):
        iters = 0
        while iters < self.num_iters:
            candidate_cols = np.where(self._counts == self._counts.max())[0]
            col = np.random.choice(candidate_cols)
            candidate_rows = self._find_candidate_rows(col)
            row = np.random.choice(candidate_rows)
            
            self.state[col] = row
            self._counts = self._count_per_col()
            self._total_count = self._counts.sum()
            print(f"iter {iters:0>5}: num violations: {self._total_count}")
            iters += 1
            if int(self._total_count) == 0:
                break
        

if __name__ == "__main__":
    # array = np.array([0,1,2])
    # solver = NQueen(array)
    solver = NQueen(200, 3000)
    print(solver._count_total())
    print(solver._count_per_col())
    solver.solve()