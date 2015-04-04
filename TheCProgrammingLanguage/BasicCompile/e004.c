#include <iostream>
#include <string>

using namespace std;

inline int countBits( unsigned int b )
{
	return (b * 0x200040008001ULL & 0x111111111111111ULL ) % 0xf;
}

int main()
{
	// char a = 0x80;
	string spades = "\u2660";
	string hearts = "\u2665";
	string clubs = "\u2663";
	string diamonds = "\u2666";
	cout << spades << "\n" << hearts << "\n" << clubs << "\n" << diamonds << "\n";

	cout << countBits(99998897);

	char a = 0x80;
	cout << a << endl;
	return 0;
}

