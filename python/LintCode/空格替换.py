class Solution:
    # @param {char[]} string: An array of Char
    # @param {int} length: The true length of the string
    # @return {int} The true length of new string
    def replaceBlank(self, str, length):
        # Write your code here
        s = str.replace(" ", "%20")
        print str, "--->", s

Solution().replaceBlank("Mr John Smith", 13)
