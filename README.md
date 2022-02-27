# Huffman compression algorithm

A [Huffman coding](https://en.wikipedia.org/wiki/Huffman_coding) compression algorithm implementation using java. It was an assignment for the Compression and Information theory course in Cairo University Faculty of Computers and Artificial Intelligence.  
This implementation theoretically can compress text and binary files, although it is not designed to.

## How to use

To compress a file run the program with the following arguments:
```shell
java -jar huffman.jar -c <filename>
```

For decompression:
```shell
java -jar huffman.jar -d <filename>
```

## Example

Using 5 paragraphs of [Lorem ipsum](https://www.lipsum.com/feed/html):  
[Input file](examples/lipsum.txt) is 2,588 bytes  
[Compressed file](examples/lipsum.txt.huffman) is 1,440 bytes


## See also

- [LZ77 implementation](https://github.com/KareemMAX/lz77)
- [LZ78 implementation](https://github.com/KareemMAX/lz78)
- [LZW implementation](https://github.com/KareemMAX/lzw)