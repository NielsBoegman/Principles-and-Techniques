import pandas as pd

class Factor:
    
    def __init__(self, df):
        self.label = df.columns[0]
        self.labels = []
        self.parents = []
        for l in df.columns:
            self.labels.append(l)
        for l in df.columns[1:-1]:
            self.parents.append(l)
        self.df = df
    
    def __mul__(self, other):
        common = []
        self_val_label = self.labels[-1]
        other_val_label = other.labels[-1]
        for i in range(len(self.labels)-1):
            for j in range(len(other.labels)-1):
                if self.labels[i] == other.labels[j]:
                    common.append(self.labels[i])
        
        new_df = pd.merge(self.get_df(), other.get_df(), on=common)
        if self_val_label == other_val_label:
            new_df[self_val_label] = new_df["{}_x".format(self_val_label)] * new_df["{}_y".format(other_val_label)]
            new_df.pop("{}_x".format(self_val_label))
            new_df.pop("{}_y".format(self_val_label))
        else:
            new_df[other_val_label] = new_df[self_val_label] * new_df[other_val_label]
            new_df.pop(self_val_label)
            new_df.pop(other_val_label)

        return Factor(new_df)
    
    def __reduce__(self, variable, value):
        self.df = self.df.drop(self.df[self.df.loc[:, variable] != value].index)

    def __eliminate__(self, variable):
        df = self.df.drop(variable, axis=1)
        labels = df.columns[:-1].tolist()
        df = df.groupby(labels, as_index=False).sum()
        return Factor(df)
    
    def get_df(self): return self.df

    def get_label(self): return self.label

    def get_parents(self): return self.parents
