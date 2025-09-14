{
  description = "Android App Development Environment";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-unstable";
    android-nixpkgs.url = "github:tadfisher/android-nixpkgs";
  };

  outputs = { self, nixpkgs, android-nixpkgs }: {

    devShells.x86_64-linux.default = nixpkgs.legacyPackages.x86_64-linux.mkShell {
      buildInputs = [
        nixpkgs.legacyPackages.x86_64-linux.openjdk17
        nixpkgs.legacyPackages.x86_64-linux.gradle
        nixpkgs.legacyPackages.x86_64-linux.kotlin
        (android-nixpkgs.sdk.x86_64-linux (sdkPkgs: with sdkPkgs; [
          cmdline-tools-latest
          build-tools-33-0-2
          platform-tools
          platforms-android-33
          emulator
        ]))
      ];
    };

  };
}
