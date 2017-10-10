# Brainf-k

Interpreter for [Brainf-k](https://en.wikipedia.org/wiki/Brainfuck), a tiny programming language containing only 8 commands.

| Command | Operation |
|:-------:| ----------|
| >       | Moves the data pointer to the right |
| <       | Moves the data pointer to the left  |
| +       | Increments the byte at the data pointer |
| -       | Decrements the byte at the data pointer |
| .       | Outputs the byte at the data pointer    |
| ,       | Accepts one byte of input               | 
| [       | If the byte at the data pointer is 0 jump the instruction pointer to the matching ] |
| ]       | If the byte at the data pointer is not 0 jump the instruction pointer back to the matching [ |

## [Examples](https://en.wikipedia.org/wiki/Brainfuck#Examples)

```java
BrainFcuk bf = new BrainFcuk();
bf.interpret("++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.");
bf.interpret(getClass().getResourcesAsStream("rot13.bf"));
> Hello World!
< abc
> nop
```

## License

[CC0](https://creativecommons.org/publicdomain/zero/1.0/)
