class Factor:
    probabilties = []
    labels = []
    
    def __init__(self, df):
        for label in df.columns:
            self.labels.append(label)
        self.probabilties = df.values
    
    def __mul__(self, other):
        return
    
    def reduce_factor(self, variable, value):
        index = self.labels.index(variable)
        for i in range(len(self.probabilties)):
            if self.probabilties[i][index] != value:
                self.probabilties.pop(i)

    def remove_entry(self, index):
        self.probabilties.pop(index)
    
    def get_probs(self): return self.probabilties

def product_factors(factor1, factor2):
    return

def marginalize_factor(factor, variable):
    return