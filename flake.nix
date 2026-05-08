    {
    description = "App";

    inputs = {
        nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
    };

    outputs = { self, nixpkgs }:
        let
            system = "x86_64-linux";
            pkgs = import nixpkgs {
                inherit system;
            };

            buildInputs = [
                        pkgs.libGL
                        pkgs.libX11
                        pkgs.libXext
                        pkgs.libXcursor
                        pkgs.libXrandr
                        pkgs.libXi
                        pkgs.libXtst
                        pkgs.libpulseaudio
                        pkgs.udev
            ];

        in {
            devShells.${system} = {
                default = pkgs.mkShell {
                    packages = buildInputs;

                    LD_LIBRARY_PATH = "${pkgs.lib.makeLibraryPath buildInputs}";

                    shellHook = ''
                    '';
                };
            };
        };
    }
