# weights of position market makers
a = 1E-6
b = 1E-3
c = a/2

p = 100   # price
ask = 101 # best ask
bid = 99  # best bid

s <- seq(-500, 500)      # position level
us <- c*abs(s^3) - a*s^3 # upper spread
ls <- c*abs(s^3) + a*s^3 # upper spread
sell <- ask + us
buy  <- bid - ls
#sell <- us
lines <- data.frame(s, us, ls)

#ls <- c*abs(s^3) + a*s^3 + b # lower spread
#us <- c*abs(s^3) - a*s^3 + b # upper spread
#bid <- p * (1-ls)
#ask <- p * (1+us)

x <- range(s)
y <- range(c(sell, buy))
plot(x, y, type='n')
#lines(s, bid)
#lines(s, ask)
lines(s, sell)
lines(s, buy)