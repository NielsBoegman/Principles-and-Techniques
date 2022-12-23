"""
@Author: Joris van Vugt, Moira Berens, Leonieke van den Bulk

Class for the implementation of the variable elimination algorithm.

"""
from factor import Factor


# Factor class
# lijst met dingen in de factor
# Combinaties als ["True", "False", "0.1"]
# en dat in 1 lijst
# use dictory index to get dataframe, then pass dataframe
class VariableElimination():

    def __init__(self, network):
        """
        Initialize the variable elimination algorithm with the specified network.
        Add more initializations if necessary.

        """
        self.network = network
    #Multiply factor with its parents recursively to get the actual chances of everything of that set of inputs.
    #Used as template to call the __mul__ function.
    def multiply_parents(self, child, parents, factors):
        for i in range(len(parents)):
            if not len(parents[i].get_parents()) == 0:
                parents2 = []
                for x in parents[i].get_parents():
                    for para in factors:
                        if para.get_label() == x:
                            parents2.append(para)
                            break
                parents[i] = self.multiply_parents(parents[i], parents2, factors)
        for parent in parents:
            child = child.__mul__(parent)
        return child 

        
    def run(self, query, observed, elim_order):
        """
        Use the variable elimination algorithm to find out the probability
        distribution of the query variable given the observed variables

        Input:
            query:      The query variable
            observed:   A dictionary of the observed variables {variable: value}
            elim_order: Either a list specifying the elimination ordering
                        or a function that will determine an elimination ordering
                        given the network during the run

        Output: A variable holding the probability distribution
                for the query variable

        """
        #First turn the dataframes into our own custom Factor class and store in a list
        factors = []
        queryfactor = None
        for key in self.network.probabilities.keys():
            factors.append(Factor(self.network.probabilities[key]))
        #Retrieve the factor of the query variable
        for fac in factors:
            if fac.get_label() == query:
                queryfactor = fac
                break
        #Get the parents of the query variable to start the process of determining the probabilities
        queryparents = []
        for parent in queryfactor.get_parents():
            for fac in factors:
                if fac.get_label() == parent:
                    queryparents.append(fac)
        #Call the multiply_parents function to update the probabilities to the required values
        queryfactor = self.multiply_parents(queryfactor,queryparents,factors)
        #Reduce to account for evidence
        for key in observed.keys():
            queryfactor.__reduce__(key,observed[key])
        labels = queryfactor.get_df().columns
        #Eliminate the variables in the given order
        for elim in elim_order:
            if elim != query and elim in labels:
                queryfactor = queryfactor.__eliminate__(elim)
        df = queryfactor.get_df()
        #Normalize the resulting factor
        multiplyfactor = 1/(df['prob'][0] + df['prob'][1])
        df['prob'][0] = df['prob'][0] * multiplyfactor
        df['prob'][1] = df['prob'][1] * multiplyfactor
        return df

        
        
            