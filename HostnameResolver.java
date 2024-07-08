import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

// Represents an address
public class HostnameResolver {
  // Stores the name of an address
  private InetAddress inet;

  // Constructor initialises state variables
  public HostnameResolver() {
    setInet(null);
  }

  // Gets the 'inet' state variable
  public InetAddress getInetAddress() {
    return this.inet;
  }

  // Gets the InetAddress
  public InetAddress getByName(String host) throws UnknownHostException {
    return InetAddress.getByName(host);
  }

  // Gets the hostname
  public String getHostName() {
    return this.inet.getHostName();
  }

  // Gets the canonical hostname
  public String getCanonicalHostName() {
    return this.inet.getCanonicalHostName();
  }

  // Gets the host address
  public String getHostAddress() {
    return this.inet.getHostAddress();
  }

  // Checks if the address is reachable
  public boolean isReachable(int timeout) throws IOException {
    return this.inet.isReachable(timeout);
  }

  // Sets the 'inet' state variable
  public void setInet(InetAddress inet) {
    this.inet = inet;
  }

  // Displays the hostname, canonical hostname, IP address and IP version of an address. Also displays whether an
  // address is reachable or not
  public void resolve(String host) throws UnknownHostException, IOException {
    setInet(getByName(host));

    System.out.println("Hostname: " + getHostName());
    System.out.println("Canonical Hostname: " + getCanonicalHostName());
    System.out.println("IP Address: " + getHostAddress());

    // Checks if an address is an IPv4 or IPv6 address
    if(inet instanceof Inet4Address) {
      System.out.println("IP Version: IPv4");
    }
    else if(inet instanceof Inet6Address) {
      System.out.println("IP Version: IPv6");
    }

    // Checks if an address is reachable
    if(isReachable(3000)) {
      System.out.println("Host is reachable.\n");
    }
    else {
      System.out.println("Host is not reachable.\n");
    }
  }

  public static void main(String args[]) {
    // Stores the IP addresses of IPv4 addresses
    List<String> ipv4Addresses = new LinkedList<>();
    HostnameResolver address = new HostnameResolver();
    int count = args.length;

    // Loops through each command line argument
		for(int i = 0; i < count; i++) {
      try {
        address.resolve(args[i]);

        if(address.getInetAddress() instanceof Inet4Address) {
          ipv4Addresses.add(address.getInetAddress().getHostAddress());
        }
      }
      catch(UnknownHostException e) {
        e.printStackTrace();
        System.out.println();
      }
      catch(IOException e) {
        System.out.println("Host is not reachable.");
      }
		}

    // Compares the hierarchy levels for every IPv4 address and outputs the highest level that all addresses share
    if(ipv4Addresses.size() > 1) {
      int index = -1;
      String sharedAddress = "";
      String[][] splitAddresses = new String[ipv4Addresses.size()][4];
      int rowCounter = 0;
      int columnCounter = 0;
      boolean isSame = false;

      for(String i: ipv4Addresses) {
        String[] bytes = i.split("\\.");

        for(String j: bytes) {
          splitAddresses[rowCounter][columnCounter] = j;
          columnCounter++;
        }

        columnCounter = 0;
        rowCounter++;
      }

      for(int i = 0; i < 4; i++) {
        for(int j = 0; j < ipv4Addresses.size() -1; j++) {

          if(splitAddresses[j][i].equals(splitAddresses[j + 1][i])) {
            isSame = true;
          }
          else {
            isSame = false;
          }
        }

        if(isSame) {
          index = i;
        }
        else {
          break;
        }
      }

      if(index == -1) {
        sharedAddress = "None";
      }
      else {
        String[] bytes = ipv4Addresses.get(0).split("\\.");
        int counter = 0;
        boolean isAsterix = false;

        for(String i: bytes) {
          if(isAsterix == true) {
            sharedAddress += "*";
          }
          else {
            sharedAddress += i;
          }

          if(counter == index) {
            isAsterix = true;
          }

          if(counter != 3) {
            sharedAddress += ".";
          }
          counter++;
        }
      }

      System.out.println("Highest hierarchy level all IPv4 addresses share: " + sharedAddress);
    }
  }
}
