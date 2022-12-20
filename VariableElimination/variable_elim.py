"""
@Author: Joris van Vugt, Moira Berens, Leonieke van den Bulk

Class for the implementation of the variable elimination algorithm.

"""

# Factor class
# lijst met dingen in de factor
# Combinaties als ["True", "False", "0.1"]
# en dat in 1 lijst
class VariableElimination():

    def __init__(self, network):
        """
        Initialize the variable elimination algorithm with the specified network.
        Add more initializations if necessary.

        """
        self.network = network

    def checkRow(one, two):
        for x in range(len(one)-1):
            if not one[x] == two[x]:
                return False
        return True

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
        #TODO create factors for johncalls etc.
        #TODO reduce to keep only observed
        for variable in elim_order:
            if variable == query:
                continue
            toelim = 0
            for i in range(len(table[0])):
                if table[0][i] ==  variable:
                    toelim = i
            for x in range(len(table)):
                table[x].pop(toelim)
            while not x == len(table):
                for y in range(len(table)):
                    if not x ==y:
                        if checkRow(table[x], table[y]):
                            table[x][-1] += table[y][-1]
                            table.pop(y)
                            y-=1
                x+=1