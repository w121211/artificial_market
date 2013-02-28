#postscript(file="artificial_market/fig/pmm_bid_ask.eps", onefile=FALSE, horizontal=FALSE, width=6, height=5, paper="special")
postscript(file="artificial_market/fig/pmm_bid_ask.eps", onefile=FALSE, horizontal=FALSE, width=5, height=5, paper="special")
#png(file="artificial_market/fig/pmm_bid_ask.png", width=600, height=500)

# weights of position market makers
TICK = 0.0005
a = 1E-7
c = a/2

p = 100   # price
ask = 100.01 # best ask
bid = 99.99  # best bid

s <- seq(-50, 50)      # position level
#us <- -(c*abs(s^3) - a*s^3) # upper spread
us <- a*s^3 -c*abs(s^3) # upper spread
#ls <- -(c*abs(s^3) + a*s^3) # upper spread
ls <- -a*s^3 -c*abs(s^3)  # upper spread
us <- round(us/TICK) * TICK
ls <- round(ls/TICK) * TICK
sell <- ask - us
buy  <- bid + ls

x <- range(s)
y <- range(c(sell, buy))
#y <- range(c(ls))
#plot(x, y, type='n', xlab='position (shares)', ylab='price')
plot(x, y, type='n', xlab='', ylab='')
#text(-40, 100.015, "ask", cex=.8)
#text(-40, 100.005, "bid", cex=.8)

# plot lines
#lines(s, s*0+ask, col="red", lty=2)
#lines(s, s*0+bid, col="blue", lty=2)
#lines(s, ls)
#lines(s, us)
lines(s, sell)
lines(s, buy, lty=1)

#legend("topright", col=c("red","blue"), lty=c(2,2), legend=c("best ask","best bid"), cex=.8)
dev.off()
