import pandas as pd

class Factor:
    labels = []
    
    def __init__(self, df):
        for label in df.columns:
            self.labels.append(label)
        self.df = df
    
    def __mul__(self, other):
        common = ""
        self_val_label = self.labels[-1]
        other_val_label = other.labels[-1]
        for i in range(len(self.labels)-1):
            for j in range(len(other.labels)-1):
                if self.labels[i] == other.labels[j]:
                    common = self.labels[i]
        
        new_df = pd.merge(self.get_df(), other.get_df(), on=common)
        print(new_df)
        if self_val_label == other_val_label:
            new_df[self_val_label] = new_df["{}_x".format(self_val_label)] * new_df["{}_y".format(other_val_label)]
            new_df.pop("{}_x".format(self_val_label))
            new_df.pop("{}_y".format(self_val_label))
        else:
            new_df[other_val_label] = new_df[self_val_label] * new_df[other_val_label]
            new_df.pop(self_val_label)
            new_df.pop(other_val_label)

        return Factor(new_df)
    
    def reduce_factor(self, variable, value):
        index = self.labels.index(variable)
        for i in range(len(self.probabilties)):
            if self.probabilties[i][index] != value:
                self.probabilties.pop(i)

    def remove_entry(self, index):
        self.probabilties.pop(index)
    
    def get_df(self): return self.df

def product_factors(factor1, factor2):
    return

def marginalize_factor(factor, variable):
    return